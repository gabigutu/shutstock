package com.db.controller;

import com.db.POJO.User;
import com.db.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable(name = "id") Integer id) {
        return userService.findById(id);
    }

    @PutMapping("/users")
    public User insert(@RequestBody User user){
        return userService.insert(user);
    }

    //update
    @PutMapping("/users/{id}")
    public User update(@RequestBody User user,@PathVariable(name = "id") Integer id) {
        user.setId(id);
        return userService.update(user);
    }

    //delete
    @DeleteMapping("/users/{id}")
    public User delete(@PathVariable(name = "id") Integer id){
        return userService.delete(id);
    }
}
