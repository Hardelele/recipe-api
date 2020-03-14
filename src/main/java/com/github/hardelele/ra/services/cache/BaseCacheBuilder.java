package com.github.hardelele.ra.services.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BaseCacheBuilder {

    private final long CACHE_EXPIRE_MINUTES = 2;

    private final long CACHE_MAX_SIZE = 1000;

    public Cache build() {
        return CacheBuilder.newBuilder()
                .maximumSize(CACHE_MAX_SIZE)
                .expireAfterWrite(CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES)
                .build();
    }
}
