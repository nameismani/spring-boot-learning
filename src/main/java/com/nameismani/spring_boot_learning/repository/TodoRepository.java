package com.nameismani.spring_boot_learning.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nameismani.spring_boot_learning.entity.TodoEntity;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<TodoEntity, String> {
    List<TodoEntity> findByUserId(Long userId);
    List<TodoEntity> findByUserIdAndCompleted(Long userId, boolean completed);
    long deleteByUserId(Long userId);
}
