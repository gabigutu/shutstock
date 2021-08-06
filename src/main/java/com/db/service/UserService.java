package com.db.service;

import com.db.POJO.User;
import com.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    public User findByUsernameAndPassword(String username, String password){
        return userRepository.findByUsernameAndPassword(username,password);
    }

    public User insert(User user) {
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
}
