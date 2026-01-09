package com.nameismani.spring_boot_learning.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nameismani.spring_boot_learning.entity.User;
import com.nameismani.spring_boot_learning.entity.UserEntity;
import com.nameismani.spring_boot_learning.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
     @Autowired
    private UserService userService;

 // Get all users
    @GetMapping
    // public List<User> getAllUsers(){
    public List<UserEntity> getAllUsers(){
             return userService.getAllUsers();
    }    

     // Save a user
    @PostMapping
    public UserEntity saveUser(@RequestBody UserEntity user) {
        return userService.saveUser(user);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserEntity updateUserById(@PathVariable Long id, @RequestBody UserEntity user) {
        return userService.updateUserById(id,user);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
