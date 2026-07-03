package com.raghav.ratelimiter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bucket {

    private String clientId;

    private long capacity;

    private long tokens;

    private long refillRate;

    private long lastRefillTime;
}