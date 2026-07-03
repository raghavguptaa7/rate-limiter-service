package com.raghav.ratelimiter.service;

import com.raghav.ratelimiter.model.Bucket;
import org.springframework.stereotype.Service;

@Service
public class TokenBucketService {

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
}