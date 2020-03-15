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
        LOGGER.info("Clean up cache");
        ingredientsCache.cleanUp();
    }

    @Override
    public void deleteByCacheKey(CacheKey cacheKey) {
        LOGGER.info("Delete cache: cacheKey = {}", cacheKey);
        ingredientsCache.delete(cacheKey);
    }

    @Override
    public IngredientEntity getByName(String name) {
        LOGGER.info("Check cache: name = {}", name);
        IngredientEntity ingredientFromCache = ingredientsCache.getIfPresent(name);
        if (ingredientFromCache == null) {
            LOGGER.info("Empty cache: id = {}", name);
            throw new NullPointerException();
        }
        LOGGER.info("Get cache: name = {}, ingredient = {}", name, ingredientFromCache);
        return ingredientFromCache;
    }

    @Override
    public IngredientEntity getById(UUID id) {
        LOGGER.info("Check cache: id = {}", id.toString());
        IngredientEntity ingredientFromCache = ingredientsCache.getIfPresent(id);
        if (ingredientFromCache == null) {
            LOGGER.info("Empty cache: id = {}", id.toString());
            throw new NullPointerException();
        }
        LOGGER.info("Get cache: id = {}, ingredient = {}", id.toString(), ingredientFromCache);
        return ingredientFromCache;
    }

    @Override
    public IngredientEntity add(CacheKey cacheKey, IngredientEntity ingredientEntity) {
        LOGGER.info("Add cache: ingredient = {}", ingredientEntity);
        ingredientsCache.put(cacheKey, ingredientEntity);
        return ingredientEntity;
    }
}
