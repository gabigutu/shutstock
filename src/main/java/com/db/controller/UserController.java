package com.db.controller;

import com.db.POJO.User;
import com.db.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;



    @GetMapping("/users")
    public ResponseEntity getAllUsers(@RequestParam(name = "token") String jwtToken) {

        List<User> userList = userService.findAll(jwtToken);
        if(userList == null) {
            ResponseEntity responseEntity = new ResponseEntity<>("Authorization not valid!", HttpStatus.FORBIDDEN);
            return responseEntity;
        }
        ResponseEntity responseEntity = new ResponseEntity<>(userList, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable(name = "id") Integer id) {
        return userService.findById(id);
    }

    @PostMapping("/users")
    public User insert(@RequestBody User user) {

        return userService.insert(user);
    }


    //update
    @PutMapping("/users/{id}")
    public User update(@RequestBody User user, @PathVariable(name = "id") Integer id) {
        user.setId(id);
        return userService.update(user);
    }

    //delete
    @DeleteMapping("/users/{id}")
    public User delete(@PathVariable(name = "id") Integer id) {
        return userService.delete(id);
    }


    @GetMapping("/users/activate")
    public User activateMethod(@RequestParam(name = "jwt") String jwt) {
        return userService.activateByEmailToken(jwt);
    }

    @PostMapping("/users/login")
    public ResponseEntity login(@RequestHeader(value = "Authorization", required = true) String auth) {
        String prefix = "Basic ";
        if (auth == null)
        {
            ResponseEntity<String> responseEntity = new ResponseEntity<>("Authorization not valid!", HttpStatus.FORBIDDEN);
            return responseEntity;
        }
        String encoded_credentials = auth.substring(prefix.length());
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(encoded_credentials);
        String decoded_credentials = new String(decodedBytes);
        String [] stringArray = decoded_credentials.split(":");
        String username = stringArray[0];
        String password = stringArray[1];
        User user = userService.findByUsernameAndPassword(username, password);
        if (user == null || !user.isActivated())
        {
            ResponseEntity<String> responseEntity = new ResponseEntity<>("Authorization not valid!", HttpStatus.FORBIDDEN);
            return responseEntity;
        }

        String jwtToken = userService.generateLoginToken(user);
        user.setJwtToken(jwtToken);
        userService.update(user);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jwtToken, HttpStatus.OK);
        return responseEntity;
    }
}
