package com.github.hardelele.ra.services.cache;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.services.IngredientService;
import com.github.hardelele.ra.utils.cache.CacheKey;
import com.github.hardelele.ra.utils.cache.DoubleKeyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IngredientCacheService implements CacheService<IngredientEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final DoubleKeyCache<IngredientEntity> ingredientsCache = new DoubleKeyCache<>();

    @Override
    public void cleanUp() {
        ingredientsCache.cleanUp();
    }

    @Override
    public void deleteByCacheKey(CacheKey cacheKey) {
        ingredientsCache.delete(cacheKey);
    }

    @Override
    public IngredientEntity getByName(String name) {
        IngredientEntity ingredientFromCache = ingredientsCache.getIfPresent(name);
        if (ingredientFromCache == null) {
            throw new NullPointerException();
        }
        return ingredientFromCache;
    }

    @Override
    public IngredientEntity getById(UUID id) {
        IngredientEntity ingredientFromCache = ingredientsCache.getIfPresent(id);
        if (ingredientFromCache == null) {
            throw new NullPointerException();
        }
        return ingredientFromCache;
    }

    @Override
    public IngredientEntity add(CacheKey cacheKey, IngredientEntity ingredientEntity) {
        ingredientsCache.put(cacheKey, ingredientEntity);
        return ingredientEntity;
    }
}
