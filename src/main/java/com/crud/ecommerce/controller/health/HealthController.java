package com.crud.ecommerce.controller.health;

import com.crud.ecommerce.business.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> healthResponse() {
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "success", true,
                "message", "E-commerce API is running",
                "endpoints", List.of("/client", "/product", "/review"),
                "methods", Map.of(
                        "GET", "/endpoint and /endpoint/{id}",
                        "POST", "/endpoint",
                        "PUT", "/endpoint/{id}",
                        "DELETE", "/endpoint/{id}"
                ),
                "timestamp", DateUtils.now()
        ));
    }
}