package com.raghav.ratelimiter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Bucket {

    private String clientId;
    private long capacity;
    private long tokens;
    private long refillRate;
    private long lastRefillTime;

    public Bucket(String clientId, long capacity, long refillRate) {
        this.clientId = clientId;
        this.capacity = capacity;
        this.tokens = capacity; // Start with a full bucket
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }
}