package com.raghav.ratelimiter.service;

import com.raghav.ratelimiter.exception.ClientAlreadyExistsException;
import com.raghav.ratelimiter.metrics.RateLimiterMetrics;
import com.raghav.ratelimiter.model.Bucket;
import com.raghav.ratelimiter.repository.BucketRepository;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class TokenBucketService {

    private final BucketRepository repository;
    private final RateLimiterMetrics metrics;

    public TokenBucketService(BucketRepository repository,
                              RateLimiterMetrics metrics) {
        this.repository = repository;
        this.metrics = metrics;
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

        metrics.incrementTotalRequests();

        Timer.Sample sample = Timer.start();

        Long result = repository.allowRequestLua(clientId);

        if (result == null) {

            metrics.incrementBlockedRequests();
            sample.stop(metrics.getRequestTimer());

            return false;
        }

        if (result == -1) {

            metrics.incrementBlockedRequests();
            sample.stop(metrics.getRequestTimer());

            return false;
        }

        if (result == -2) {

            metrics.incrementBlockedRequests();
            sample.stop(metrics.getRequestTimer());

            return false;
        }

        metrics.incrementAllowedRequests();
        sample.stop(metrics.getRequestTimer());

        return true;
    }

    public Bucket getBucket(String clientId) {

        return repository.findByClientId(clientId);

    }
}