package com.nameismani.spring_boot_learning.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.nameismani.spring_boot_learning.entity.User;
import com.nameismani.spring_boot_learning.entity.UserEntity;
import com.nameismani.spring_boot_learning.repository.UserRepository;

import java.util.List;

@Service  
public class UserService {
     @Autowired
    private UserRepository userRepository;

    // Get all users
    // public List<User> getAllUsers() {
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Save a user
    // public User saveUser(User user) {
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    // Get a user by ID
    // public User getUserById(String id) {
    public UserEntity getUserById(Long  id) {
        return userRepository.findById(id).orElse(null); // Return null if not found
    }

    // Delete a user by ID
    // public void deleteUser(String id) {
    public void deleteUser(Long  id) {
        userRepository.deleteById(id);
    }
}
