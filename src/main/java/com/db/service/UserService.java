package com.db.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.db.POJO.User;
import com.db.mail.EmailService;
import com.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User insert(User user) {
        user.setUID(UUID.randomUUID().toString());
        String emailBody = "This is your activation key: " + generateEmailActivationToken(user);
        emailService.sendSimpleMessage(user.getEmail(), "Please Activate Your Account", emailBody);

        user.setRole('0');
        return userRepository.save(user);
    }

    public User update(User user) {
        User user1 = this.findById(user.getId());
        user1.setEmail(user.getEmail());
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
//        if (user1.getRole() == '0') {
//            user.setRole('0');
//        }
        //TODO implement roles
        return userRepository.save(user1);
    }

    public User delete(int id) {
        User user = this.findById(id);
        userRepository.delete(user);
        return user;
    }

    private String generateEmailActivationToken(User user) {
        try {
            byte[] secret = Base64.getDecoder().decode("c2VjcmV0Rm9yTXlKd3RFeGFtcGxlQmFzZTY0Rm9ybWF0");

            Algorithm algorithm = Algorithm.HMAC256(secret);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date dateNow = new Date(System.currentTimeMillis());
            System.out.println(formatter.format(dateNow));

            Date expireDate = new Date(System.currentTimeMillis() + (1000 * 60 * 30));

            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", user.getUsername())
                    .withClaim("UID", user.getUID())

                    .withIssuedAt(dateNow)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);


            return token;
        } catch (JWTCreationException exception) {
            System.out.println("crapat createToken");
        }
        return null;
    }

    private String generateLoginToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("bancu");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date dateNow = new Date(System.currentTimeMillis());
            System.out.println(formatter.format(dateNow));

            Date expireDate = new Date(System.currentTimeMillis() + (1000 * 60 * 30));

            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", user.getUsername())
                    .withClaim("UID", user.getUID())
                    .withIssuedAt(dateNow)
                    .withExpiresAt(expireDate)
                    //    .withClaim("password", (String) null)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            System.out.println("crapat createToken");
        }
        return null;
    }

    public User activateByEmailToken(String emailToken) {
        try{
            DecodedJWT jwtDecoded = JWT.decode(emailToken);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date dateNow = new Date(System.currentTimeMillis());
            System.out.println(formatter.format(dateNow));

            // verify if expired
            if(jwtDecoded.getExpiresAt().after(dateNow)) {
                // verify username + UID coincide in DB
                String uid = jwtDecoded.getClaim("UID").asString();

                User user = userRepository.findByUID(uid);
                if(user!=null) {
                   user.setActivated(true);
                   userRepository.save(user);
                   return user;
                }
            }
        }catch(JWTDecodeException e){
            System.out.println("JWTDecode ERROR !");
        }
       return null;
    }
}
