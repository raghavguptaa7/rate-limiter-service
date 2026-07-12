package com.raghav.ratelimiter.repository;

import com.raghav.ratelimiter.model.Bucket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBucketRepository implements BucketRepository {

    private final Map<String, Bucket> buckets =
            new ConcurrentHashMap<>();

    @Override
    public void save(Bucket bucket) {
        buckets.put(bucket.getClientId(), bucket);
    }

    @Override
    public Bucket findByClientId(String clientId) {
        return buckets.get(clientId);
    }

    @Override
    public boolean exists(String clientId) {
        return buckets.containsKey(clientId);
    }

    @Override
    public void delete(String clientId) {
        buckets.remove(clientId);
    }
}