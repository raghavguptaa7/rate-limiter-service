package com.raghav.ratelimiter.controller;

import com.raghav.ratelimiter.dto.RateLimitRequest;
import com.raghav.ratelimiter.dto.RateLimitResponse;
import com.raghav.ratelimiter.dto.RegisterClientRequest;
import com.raghav.ratelimiter.model.Bucket;
import com.raghav.ratelimiter.service.TokenBucketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RateLimiterController {

    private final TokenBucketService tokenBucketService;

    public RateLimiterController(TokenBucketService tokenBucketService) {
        this.tokenBucketService = tokenBucketService;
    }

    @PostMapping("/clients")
    public String registerClient(
            @Valid @RequestBody RegisterClientRequest request) {

        tokenBucketService.registerClient(
                request.getClientId(),
                request.getCapacity(),
                request.getRefillRate()
        );

        return "Client Registered Successfully";
    }

    @PostMapping("/allow")
    public RateLimitResponse allowRequest(
            @Valid @RequestBody RateLimitRequest request) {

        boolean allowed =
                tokenBucketService.allowRequest(request.getClientId());

        Bucket bucket =
                tokenBucketService.getBucket(request.getClientId());

        long remaining =
                bucket == null ? 0 : bucket.getTokens();

        return new RateLimitResponse(
                allowed,
                remaining
        );
    }

}