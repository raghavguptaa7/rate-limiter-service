package com.raghav.ratelimiter.repository;

import com.raghav.ratelimiter.model.Bucket;

public interface BucketRepository {

    void save(Bucket bucket);

    Bucket findByClientId(String clientId);

    boolean exists(String clientId);

    void delete(String clientId);

    Long allowRequestLua(String clientId);
}