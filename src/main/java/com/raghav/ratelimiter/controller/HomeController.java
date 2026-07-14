package com.raghav.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return """
                🚀 Token Bucket Rate Limiter API

                Application Status : Running ✅

                Swagger UI:
                /swagger-ui/index.html

                Health:
                /actuator/health
                """;
    }
}