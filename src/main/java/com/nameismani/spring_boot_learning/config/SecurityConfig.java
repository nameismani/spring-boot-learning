package com.nameismani.spring_boot_learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter,
                          UserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth -> 
            auth.requestMatchers(HttpMethod.POST,"/api/users")
            .permitAll()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/users/**").authenticated()
            .anyRequest().permitAll())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->  sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  )
            .addFilterBefore(  jwtFilter, UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

    // CHANGE 1: Fix DaoAuthenticationProvider constructor - use UserDetailsService constructor instead of deprecated no-arg
    // This fixes "constructor DaoAuthenticationProvider() is undefined" error
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        // OLD (causing errors): DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // authProvider.setUserDetailsService(userDetailsService);
        // authProvider.setPasswordEncoder(passwordEncoder);

        // NEW: Use constructor that takes UserDetailsService directly (Spring Security 6+ API)
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // CHANGE 2: Add AuthenticationManager bean - this fixes "No qualifying bean of type 'AuthenticationManager'" error
    // This exposes AuthenticationManager so AuthController constructor injection works
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
