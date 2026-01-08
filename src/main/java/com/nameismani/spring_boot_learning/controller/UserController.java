package com.nameismani.spring_boot_learning.controller;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nameismani.spring_boot_learning.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public List<User> getUsers(){
        return Arrays.asList(new User(1L,"Mani","nameismani@gmail.com"),new User(2L,"Sakthi","sakthi@gmail.com"));
    }    

}
