package com.raghav.ratelimiter.service;

import com.raghav.ratelimiter.exception.ClientAlreadyExistsException;
import com.raghav.ratelimiter.model.Bucket;
import com.raghav.ratelimiter.repository.BucketRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenBucketService {

    private final BucketRepository repository;

    public TokenBucketService(BucketRepository repository) {
        this.repository = repository;
    }

    public void registerClient(String clientId,
                               long capacity,
                               long refillRate) {

        if (repository.exists(clientId)) {
            throw new ClientAlreadyExistsException("Client already exists");
        }

        Bucket bucket = new Bucket(
                clientId,
                capacity,
                refillRate
        );

        repository.save(bucket);
    }

    public boolean allowRequest(String clientId) {

        Bucket bucket = repository.findByClientId(clientId);

        if (bucket == null) {
            return false;
        }

        bucket.getLock().lock();

        try {

            refillTokens(bucket);

            if (bucket.getTokens() > 0) {
                bucket.consumeToken();
                return true;
            }

            return false;

        } finally {
            bucket.getLock().unlock();
        }
    }

    public Bucket getBucket(String clientId) {
        return repository.findByClientId(clientId);
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