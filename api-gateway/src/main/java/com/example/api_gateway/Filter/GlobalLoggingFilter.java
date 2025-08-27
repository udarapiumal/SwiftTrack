package com.example.api_gateway.Filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Log incoming request
        System.out.println("Global Filter - Request: " + request.getMethod() + " " + request.getURI() +
                " at " + LocalDateTime.now());

        // Add custom headers
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Gateway-Timestamp", LocalDateTime.now().toString())
                .header("X-Request-Id", java.util.UUID.randomUUID().toString())
                .build();

        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(modifiedRequest)
                .build();

        return chain.filter(modifiedExchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            System.out.println("Global Filter - Response Status: " + response.getStatusCode() +
                    " for " + request.getURI());
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}