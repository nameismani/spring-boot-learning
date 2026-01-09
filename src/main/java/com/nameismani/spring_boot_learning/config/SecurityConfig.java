package com.nameismani.spring_boot_learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nameismani.spring_boot_learning.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
       @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
  http.authorizeHttpRequests(auth -> 
    auth.requestMatchers(HttpMethod.POST,"/api/users")
    .permitAll().requestMatchers("/api/users/**").authenticated()
    .anyRequest().permitAll())
    .csrf(csrf -> csrf.disable())
    .formLogin(form-> form.permitAll())
    ;

  return http.build();
    }

    // @Bean
    // public UserDetailsService userDetailsService(){
    //     // UserDetails user = User.withUsername("alice")
    //     // .password(passwordEncoder.encode("user123")).roles("USER").build();

    //     // return new InMemoryUserDetailsManager(user);
    //     return CustomUserDetailService();
    // }

@Bean
public DaoAuthenticationProvider authenticationProvider(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider authProvider =
            new DaoAuthenticationProvider(userDetailsService);

    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
}
}
