package com.nameismani.spring_boot_learning.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nameismani.spring_boot_learning.config.JwtSecurity;
import com.nameismani.spring_boot_learning.entity.UserEntity;
import com.nameismani.spring_boot_learning.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private  AuthenticationManager authManager;

    @Autowired
    private  JwtSecurity jwtSecurity;

      @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody UserEntity user){
     try{
            Authentication authentication= authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String token =jwtSecurity.generateToken(userDetails);

    return ResponseEntity.ok(Map.of("token",token));
     }catch(Exception e){
       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid user name and password"));
     }
    }

     @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Email already exists"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Default role
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User created successfully"));
    }
}
