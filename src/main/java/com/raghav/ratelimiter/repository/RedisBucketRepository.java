package com.raghav.ratelimiter.repository;

import com.raghav.ratelimiter.model.Bucket;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RedisBucketRepository implements BucketRepository {

    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> tokenBucketScript;

    public RedisBucketRepository(
            StringRedisTemplate redisTemplate,
            DefaultRedisScript<Long> tokenBucketScript) {

        this.redisTemplate = redisTemplate;
        this.tokenBucketScript = tokenBucketScript;
    }

    private String getKey(String clientId) {
        return "bucket:" + clientId;
    }

    @Override
    public void save(Bucket bucket) {

        String key = getKey(bucket.getClientId());

        redisTemplate.opsForHash().put(key, "clientId", bucket.getClientId());
        redisTemplate.opsForHash().put(key, "capacity", String.valueOf(bucket.getCapacity()));
        redisTemplate.opsForHash().put(key, "tokens", String.valueOf(bucket.getTokens()));
        redisTemplate.opsForHash().put(key, "refillRate", String.valueOf(bucket.getRefillRate()));
        redisTemplate.opsForHash().put(key, "lastRefillTime", String.valueOf(bucket.getLastRefillTime()));
    }

    @Override
    public Bucket findByClientId(String clientId) {

        String key = getKey(clientId);

        Map<Object, Object> values = redisTemplate.opsForHash().entries(key);

        if (values.isEmpty()) {
            return null;
        }

        Bucket bucket = new Bucket();

        bucket.setClientId(clientId);
        bucket.setCapacity(Long.parseLong(values.get("capacity").toString()));
        bucket.setTokens(Long.parseLong(values.get("tokens").toString()));
        bucket.setRefillRate(Long.parseLong(values.get("refillRate").toString()));
        bucket.setLastRefillTime(Long.parseLong(values.get("lastRefillTime").toString()));

        return bucket;
    }

    @Override
    public boolean exists(String clientId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getKey(clientId)));
    }

    @Override
    public void delete(String clientId) {
        redisTemplate.delete(getKey(clientId));
    }

    @Override
    public Long allowRequestLua(String clientId) {

        return redisTemplate.execute(
                tokenBucketScript,
                List.of(getKey(clientId)),
                String.valueOf(System.currentTimeMillis())
        );
    }
}