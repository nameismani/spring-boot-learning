package com.nameismani.spring_boot_learning.controller;

import com.nameismani.spring_boot_learning.entity.TodoEntity;
import com.nameismani.spring_boot_learning.entity.UserEntity;
import com.nameismani.spring_boot_learning.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    // ✅ CREATE Todo (Authenticated)
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTodo(
            HttpServletRequest request,
            @RequestBody TodoEntity todo) {
        
        UserEntity currentUser = (UserEntity) request.getAttribute("currentUser");
        
        TodoEntity savedTodo = todoService.createTodo(todo, currentUser.getId());
        
        return ResponseEntity.ok(Map.of(
            "message", "Todo created successfully",
            "todo", savedTodo
        ));
    }

    // ✅ GET All user's todos
    @GetMapping
    public ResponseEntity<Map<String, Object>> getTodos(HttpServletRequest request) {
        UserEntity currentUser = (UserEntity) request.getAttribute("currentUser");
        List<TodoEntity> todos = todoService.getTodosByUser(currentUser.getId());
        
        return ResponseEntity.ok(Map.of(
            "todos", todos,
            "total", todos.size(),
            "completed", todoService.getCompletedTodos(currentUser.getId()).size()
        ));
    }

    // ✅ UPDATE Todo
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTodo(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody TodoEntity todo) {
        
        UserEntity currentUser = (UserEntity) request.getAttribute("currentUser");
        TodoEntity updatedTodo = todoService.updateTodo(id, todo, currentUser.getId());
        
        return ResponseEntity.ok(Map.of(
            "message", "Todo updated successfully",
            "todo", updatedTodo
        ));
    }

    // ✅ DELETE Todo
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTodo(
            HttpServletRequest request,
            @PathVariable String id) {
        
        UserEntity currentUser = (UserEntity) request.getAttribute("currentUser");
        todoService.deleteTodo(id, currentUser.getId());
        
        return ResponseEntity.ok(Map.of("message", "Todo deleted successfully"));
    }

    // ✅ DELETE ALL todos (Dangerous!)
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteAllTodos(HttpServletRequest request) {
        UserEntity currentUser = (UserEntity) request.getAttribute("currentUser");
        todoService.deleteAllTodos(currentUser.getId());
        return ResponseEntity.ok(Map.of("message", "All todos deleted"));
    }
}
