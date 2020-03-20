package com.github.hardelele.ra.services.cache;

import com.github.hardelele.ra.utils.cache.BaseCacheBuilder;
import com.google.common.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CacheService<E> {

    private BaseCacheBuilder baseCacheBuilder = new BaseCacheBuilder();

    private Cache<CacheKey, E> cache = baseCacheBuilder.build();

    public void cleanUp() {
        cache.invalidateAll();
    }

    public void delete(UUID id, Class<E> entityClass) {
        CacheKey cacheKey = new CacheKey(id, entityClass);
        cache.invalidate(cacheKey);
    }

    public E get(UUID id, Class<E> entityClass) {
        CacheKey cacheKey = new CacheKey(id, entityClass);
        return cache.getIfPresent(cacheKey);
    }

    public E add(UUID id, E entity) {
        CacheKey cacheKey = new CacheKey(id, (Class<E>) entity.getClass());
        cache.put(cacheKey, entity);
        return entity;
    }

    @Data
    @AllArgsConstructor
    private class CacheKey {
        private UUID id;
        private Class<E> entityClass;
    }
}
