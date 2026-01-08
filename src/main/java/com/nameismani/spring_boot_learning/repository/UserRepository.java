package com.nameismani.spring_boot_learning.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.nameismani.spring_boot_learning.entity.User;


public interface UserRepository extends MongoRepository<User, String> {
    // Custom query methods can be added here
    User findByName(String name); // Example custom query method
}