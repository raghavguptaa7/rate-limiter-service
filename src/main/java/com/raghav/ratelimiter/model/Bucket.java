package com.raghav.ratelimiter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
@NoArgsConstructor
public class Bucket {

    private String clientId;
    private long capacity;
    private long tokens;
    private long refillRate;
    private long lastRefillTime;

    private final ReentrantLock lock = new ReentrantLock();

    public Bucket(String clientId,
                  long capacity,
                  long refillRate) {

        this.clientId = clientId;
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public void consumeToken() {
        if (tokens > 0) {
            tokens--;
        }
    }

    public ReentrantLock getLock() {
        return lock;
    }
}