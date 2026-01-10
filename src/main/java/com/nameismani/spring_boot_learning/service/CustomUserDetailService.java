package com.nameismani.spring_boot_learning.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nameismani.spring_boot_learning.entity.UserEntity;
import com.nameismani.spring_boot_learning.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
   UserEntity user =  userRepository.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException("User not found with email: " + email)
        );
    return user;
   }
}
