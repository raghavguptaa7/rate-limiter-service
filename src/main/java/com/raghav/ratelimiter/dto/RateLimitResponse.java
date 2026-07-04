package com.raghav.ratelimiter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateLimitResponse {

    private boolean allowed;
    private long remainingTokens;
}