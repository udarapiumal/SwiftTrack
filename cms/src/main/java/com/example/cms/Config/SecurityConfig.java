package com.example.cms.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public SecurityConfig() {
        System.out.println("=== SecurityConfig CONSTRUCTOR CALLED ===");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("=== CONFIGURING SECURITY FILTER CHAIN ===");
        System.out.println("=== DISABLING CSRF COMPLETELY ===");

        http
                .csrf(csrf -> {
                    System.out.println("=== CSRF DISABLED ===");
                    csrf.disable();
                })
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        System.out.println("=== SECURITY CONFIGURATION COMPLETE ===");
        return http.build();
    }
}