package com.github.hardelele.ra.services.database;

import com.github.hardelele.ra.utils.cache.CacheKey;

import java.util.List;
import java.util.UUID;

public interface DatabaseService<E> {

    List<E> getAll();
    void cleanUp();
    CacheKey delete(UUID id);
    E add(E entity);
    E getByName(String name);
    E getById(UUID id);
    boolean isExistByName(String name);
}
