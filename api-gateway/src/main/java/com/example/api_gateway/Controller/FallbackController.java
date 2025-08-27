package com.example.api_gateway.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/cms")
    @PostMapping("/cms")
    public ResponseEntity<Map<String, Object>> cmsFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "CMS Service is currently unavailable",
                        "message", "Please try again later",
                        "timestamp", LocalDateTime.now(),
                        "service", "cms-service"
                ));
    }

    @GetMapping("/service2")
    @PostMapping("/service2")
    public ResponseEntity<Map<String, Object>> service2Fallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Service 2 is currently unavailable",
                        "message", "Please try again later",
                        "timestamp", LocalDateTime.now(),
                        "service", "service-2"
                ));
    }

    @GetMapping("/service3")
    @PostMapping("/service3")
    public ResponseEntity<Map<String, Object>> service3Fallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Service 3 is currently unavailable",
                        "message", "Please try again later",
                        "timestamp", LocalDateTime.now(),
                        "service", "service-3"
                ));
    }
}