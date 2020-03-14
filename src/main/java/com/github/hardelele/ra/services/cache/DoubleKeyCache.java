package com.github.hardelele.ra.services.cache;

import com.github.hardelele.ra.services.cache.keys.CacheKey;
import com.google.common.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DoubleKeyCache<V> {

    private final BaseCacheBuilder baseCacheBuilder = new BaseCacheBuilder();

    private final Cache<UUID, V> entityCacheById = baseCacheBuilder.build();
    private final Cache<String, V> entityCacheByName = baseCacheBuilder.build();

    public V getIfPresent(UUID id) {
        return entityCacheById.getIfPresent(id);
    }

    public V getIfPresent(String name) {
        return entityCacheByName.getIfPresent(name);
    }

    public void put(CacheKey key, V value) {
        UUID id = key.getId();
        entityCacheById.put(id, value);
        String name = key.getName();
        entityCacheByName.put(name, value);
    }
}
