package com.nameismani.spring_boot_learning.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {  // ✅ REMOVED implements UserDetails
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String role = "USER"; // Default: USER

    // ✅ REMOVED ALL Spring Security methods & imports
    // No getAuthorities(), getUsername(), UserDetails methods needed!

    // Keep your existing getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Constructors (keep as-is)
    public UserEntity() {}
    public UserEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
