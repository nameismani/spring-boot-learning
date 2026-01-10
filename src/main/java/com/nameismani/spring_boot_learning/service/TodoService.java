
package com.nameismani.spring_boot_learning.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nameismani.spring_boot_learning.entity.TodoEntity;
import com.nameismani.spring_boot_learning.repository.TodoRepository;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public TodoEntity createTodo(TodoEntity todo, Long userId) {
        todo.setUserId(userId);
        return todoRepository.save(todo);
    }

    public List<TodoEntity> getTodosByUser(Long userId) {
        return todoRepository.findByUserId(userId);
    }

    public List<TodoEntity> getCompletedTodos(Long userId) {
        return todoRepository.findByUserIdAndCompleted(userId, true);
    }

    public TodoEntity updateTodo(String id, TodoEntity todoUpdate, Long userId) {
        TodoEntity existing = todoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Not your todo");
        }
        
        existing.setTitle(todoUpdate.getTitle());
        existing.setDescription(todoUpdate.getDescription());
        existing.setCompleted(todoUpdate.isCompleted());
        existing.setTags(todoUpdate.getTags());
        
        return todoRepository.save(existing);
    }

    public void deleteTodo(String id, Long userId) {
        TodoEntity todo = todoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        if (!todo.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Not your todo");
        }
        
        todoRepository.deleteById(id);
    }

    public void deleteAllTodos(Long userId) {
        todoRepository.deleteByUserId(userId);
    }
}
