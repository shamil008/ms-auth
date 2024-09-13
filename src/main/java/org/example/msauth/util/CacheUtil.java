package org.example.msauth.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;


import static java.util.concurrent.TimeUnit.MINUTES;
import static org.example.msauth.model.constants.CacheConstants.CACHE_PREFIX;

@Component
@RequiredArgsConstructor
public class CacheUtil {
    private final RedissonClient redissonClient;

    public <T> T getBucket(String cacheKey) {
        RBucket<T> bucket = getRBucket(cacheKey);
        return bucket == null ? null : bucket.get();
    }

    public <T> void updateToCache(T value, String key, Long expireTime) {
        var bucket = getRBucket(key);
        bucket.set(value, expireTime, MINUTES);
    }

    private <T> RBucket<T> getRBucket(String cacheKey) {
        var key = CACHE_PREFIX.formatted(cacheKey);
        return redissonClient.getBucket(key);
    }
}
