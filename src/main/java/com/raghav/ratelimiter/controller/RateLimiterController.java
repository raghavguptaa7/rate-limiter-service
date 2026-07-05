package com.raghav.ratelimiter.controller;

import com.raghav.ratelimiter.dto.RateLimitRequest;
import com.raghav.ratelimiter.dto.RateLimitResponse;
import com.raghav.ratelimiter.dto.RegisterClientRequest;
import com.raghav.ratelimiter.model.Bucket;
import com.raghav.ratelimiter.service.TokenBucketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(
        name = "Rate Limiter",
        description = "APIs for registering clients and checking request limits"
)
public class RateLimiterController {

    private final TokenBucketService tokenBucketService;

    public RateLimiterController(TokenBucketService tokenBucketService) {
        this.tokenBucketService = tokenBucketService;
    }

    @Operation(
            summary = "Register Client",
            description = "Creates a new token bucket for a client."
    )
    @PostMapping("/clients")
    public ResponseEntity<String> registerClient(
            @Valid @RequestBody RegisterClientRequest request) {

        tokenBucketService.registerClient(
                request.getClientId(),
                request.getCapacity(),
                request.getRefillRate()
        );

        return ResponseEntity.ok("Client Registered Successfully");
    }

    @Operation(
            summary = "Allow Request",
            description = "Consumes one token if available."
    )
    @PostMapping("/allow")
    public ResponseEntity<RateLimitResponse> allowRequest(
            @Valid @RequestBody RateLimitRequest request) {

        boolean allowed =
                tokenBucketService.allowRequest(request.getClientId());

        Bucket bucket =
                tokenBucketService.getBucket(request.getClientId());

        long remainingTokens = 0;

        if (bucket != null) {
            remainingTokens = bucket.getTokens();
        }

        return ResponseEntity.ok(
                new RateLimitResponse(
                        allowed,
                        remainingTokens
                )
        );
    }
}