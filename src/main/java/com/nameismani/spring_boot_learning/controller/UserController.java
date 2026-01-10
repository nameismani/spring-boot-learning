package com.nameismani.spring_boot_learning.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> saveUser(@RequestBody UserEntity user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Email already exists"));
        }
         UserEntity savedUser = userService.saveUser(user);
        return ResponseEntity.ok(Map.of("user", savedUser));
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
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
       return userService.deleteUser(id);
    }
}
