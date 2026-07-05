package com.raghav.ratelimiter.repository;

import com.raghav.ratelimiter.model.Bucket;

public interface BucketRepository {

    Bucket findByClientId(String clientId);

    void save(Bucket bucket);

    boolean exists(String clientId);

}