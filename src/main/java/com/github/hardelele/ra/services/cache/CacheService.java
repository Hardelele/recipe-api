package com.github.hardelele.ra.services.cache;

import com.github.hardelele.ra.utils.cache.CacheKey;

import java.util.UUID;

public interface CacheService<E> {

    void cleanUp();
    void deleteByCacheKey(CacheKey cacheKey);
    E getByName(String name);
    E getById(UUID id);
    E add(CacheKey cacheKey, E entity);
}
