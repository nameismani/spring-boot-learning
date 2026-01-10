package com.nameismani.spring_boot_learning.entity;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;

@Entity
@Table(name = "users")

public class UserEntity implements UserDetails {
    
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String role = "USER"; // Default: USER


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }


    public String getUsername() {
        return email;
    }

    // public boolean isAccountNonExpired() { return true; }

    // public boolean isAccountNonLocked() { return true; }

    // public boolean isCredentialsNonExpired() { return true; }

    // public boolean isEnabled() { return true; }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Constructors
    public UserEntity() {}

    public UserEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


}
