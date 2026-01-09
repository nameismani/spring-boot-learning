package com.nameismani.spring_boot_learning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.mongodb.repository.MongoRepository;
import com.nameismani.spring_boot_learning.entity.User;
import com.nameismani.spring_boot_learning.entity.UserEntity;


// public interface UserRepository extends MongoRepository<User, String> {
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Custom query methods can be added here
    User findByName(String name); // Example custom query method
}