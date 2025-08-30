package com.example.api_gateway.Config; // Adjust this to match your Gateway's actual package structure

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    public GatewaySecurityConfig() {
        System.out.println("=== GATEWAY SecurityConfig CONSTRUCTOR CALLED ===");
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.out.println("=== GATEWAY CONFIGURING SECURITY FILTER CHAIN ===");
        System.out.println("=== GATEWAY DISABLING CSRF COMPLETELY ===");

        return http
                .csrf(csrf -> {
                    System.out.println("=== GATEWAY CSRF DISABLED ===");
                    csrf.disable();
                })
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .build();
    }
}