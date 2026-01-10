package com.nameismani.spring_boot_learning.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

// import org.springframework.data.mongodb.repository.MongoRepository;
import com.nameismani.spring_boot_learning.entity.User;
import com.nameismani.spring_boot_learning.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


// public interface UserRepository extends MongoRepository<User, String> {
@Component
public interface UserRepository extends  JpaRepository<UserEntity, Long>  {
    // Custom query methods can be added here
    UserEntity findByName(String name); // Example custom query method
    Optional<UserEntity> findByEmail(String email); // Example custom query method
}