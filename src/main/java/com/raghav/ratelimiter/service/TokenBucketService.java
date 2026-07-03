package com.raghav.ratelimiter.service;

import com.raghav.ratelimiter.model.Bucket;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenBucketService {

    private final Map<String, Bucket> buckets = new HashMap<>();


    public boolean allowRequest(Bucket bucket) {

        refillTokens(bucket);

        if (bucket.getTokens() > 0) {
            bucket.setTokens(bucket.getTokens() - 1);
            return true;
        }

        return false;
    }

    private void refillTokens(Bucket bucket) {
        long currentTime = System.currentTimeMillis();

        long elapsedTime = currentTime - bucket.getLastRefillTime();

        long elapsedSeconds = elapsedTime / 1000;

        long newTokens = elapsedSeconds * bucket.getRefillRate();

        long updatedTokens = Math.min(
                bucket.getCapacity(),
                bucket.getTokens() + newTokens
        );

        bucket.setTokens(updatedTokens);
        bucket.setLastRefillTime(currentTime);
    }
    public void registerClient(String clientId,
                               long capacity,
                               long refillRate) {

        Bucket bucket = new Bucket(clientId, capacity, refillRate);

        buckets.put(clientId, bucket);
    }
}