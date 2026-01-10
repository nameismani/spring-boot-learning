package com.nameismani.spring_boot_learning.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// import com.nameismani.spring_boot_learning.entity.User;
import com.nameismani.spring_boot_learning.entity.UserEntity;
import com.nameismani.spring_boot_learning.exceptions.ResourceNotFoundException;
import com.nameismani.spring_boot_learning.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service  
public class UserService {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

      //NEW: Email check method
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get all users
    // public List<User> getAllUsers() {
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Save a user
    // public User saveUser(User user) {
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Get a user by ID
    // public User getUserById(String id) {
    public UserEntity getUserById(Long  id) {
        // return userRepository.findById(id).orElse(null); // Return null if not found
         return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found with this id" +" "+ id)); // Return Not found message
    }

    public UserEntity updateUserById(Long  id,UserEntity user) {
        // return userRepository.findById(id).orElse(null); // Return null if not found
      UserEntity userData = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found with this id" +" "+ id)); // Return Not found message
    if (user.getName() != null) {
        userData.setName(user.getName());
    }

    if (user.getEmail() != null) {
        userData.setEmail(user.getEmail());
    }

    return userRepository.save(userData);
    }


    // Delete a user by ID
    // public void deleteUser(String id) {
    public ResponseEntity<?> deleteUser(Long  id) {
         UserEntity userData = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found with this id" +" "+ id));
        userRepository.delete(userData);
        return ResponseEntity.ok().build();
    }
}
