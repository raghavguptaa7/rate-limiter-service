package com.raghav.ratelimiter.service;

import com.raghav.ratelimiter.exception.ClientAlreadyExistsException;
import com.raghav.ratelimiter.model.Bucket;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenBucketService {

    private final Map<String, Bucket> buckets = new HashMap<>();

    public void registerClient(String clientId,
                               long capacity,
                               long refillRate) {

        if (buckets.containsKey(clientId)) {
            throw new ClientAlreadyExistsException("Client already exists");
        }

        Bucket bucket = new Bucket(
                clientId,
                capacity,
                refillRate
        );

        buckets.put(clientId, bucket);
    }

    public boolean allowRequest(String clientId) {

        Bucket bucket = buckets.get(clientId);

        if (bucket == null) {
            return false;
        }

        refillTokens(bucket);

        if (bucket.getTokens() > 0) {
            bucket.consumeToken();
            return true;
        }

        return false;
    }

    public Bucket getBucket(String clientId) {
        return buckets.get(clientId);
    }

    private void refillTokens(Bucket bucket) {

        long currentTime = System.currentTimeMillis();

        long elapsedTime = currentTime - bucket.getLastRefillTime();

        long elapsedSeconds = elapsedTime / 1000;

        if (elapsedSeconds <= 0) {
            return;
        }

        long newTokens = elapsedSeconds * bucket.getRefillRate();

        bucket.setTokens(
                Math.min(
                        bucket.getCapacity(),
                        bucket.getTokens() + newTokens
                )
        );

        bucket.setLastRefillTime(
                bucket.getLastRefillTime() + elapsedSeconds * 1000
        );
    }

}