package com.raghav.ratelimiter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterClientRequest {

    @NotBlank
    private String clientId;

    @Positive
    private long capacity;

    @Positive
    private long refillRate;
}