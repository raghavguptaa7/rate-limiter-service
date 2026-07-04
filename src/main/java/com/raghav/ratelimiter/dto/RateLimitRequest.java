package com.raghav.ratelimiter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateLimitRequest {

    @NotBlank
    private String clientId;
}