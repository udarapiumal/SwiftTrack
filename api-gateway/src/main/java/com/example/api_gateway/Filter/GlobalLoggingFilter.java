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
import java.util.UUID;

@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Log incoming request
        System.out.println("Global Filter - Request: " + request.getMethod() + " " + request.getURI() +
                " Content-Type: " + request.getHeaders().getContentType() +
                " at " + LocalDateTime.now());

        // Build new request and preserve important headers
        ServerHttpRequest.Builder requestBuilder = request.mutate()
                .header("X-Gateway-Timestamp", LocalDateTime.now().toString())
                .header("X-Request-Id", UUID.randomUUID().toString());

        // Preserve Authorization header
        if (request.getHeaders().containsKey("Authorization")) {
            requestBuilder.header("Authorization", request.getHeaders().getFirst("Authorization"));
        }

        // Preserve Content-Type header (crucial for SOAP)
        if (request.getHeaders().getContentType() != null) {
            requestBuilder.header("Content-Type", request.getHeaders().getContentType().toString());
        }

        // Preserve SOAPAction header (if present)
        if (request.getHeaders().containsKey("SOAPAction")) {
            requestBuilder.header("SOAPAction", request.getHeaders().getFirst("SOAPAction"));
        }

        // Preserve Accept header
        if (request.getHeaders().containsKey("Accept")) {
            requestBuilder.header("Accept", request.getHeaders().getFirst("Accept"));
        }

        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(requestBuilder.build())
                .build();

        // Process request and log response
        return chain.filter(modifiedExchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            System.out.println("Global Filter - Response Status: " + response.getStatusCode() +
                    " Content-Type: " + response.getHeaders().getContentType() +
                    " for " + request.getURI());
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}