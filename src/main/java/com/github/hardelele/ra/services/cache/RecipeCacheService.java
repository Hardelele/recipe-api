package com.github.hardelele.ra.services.cache;

import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.services.IngredientService;
import com.github.hardelele.ra.utils.cache.CacheKey;
import com.github.hardelele.ra.utils.cache.DoubleKeyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecipeCacheService implements CacheService<RecipeEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final DoubleKeyCache<RecipeEntity> recipesCache = new DoubleKeyCache<>();

    public void cleanUp() {
        recipesCache.cleanUp();
        LOGGER.info("Clean up cache");
    }

    @Override
    public void deleteByCacheKey(CacheKey cacheKey) {
        recipesCache.delete(cacheKey);
        LOGGER.info("Delete cache: cacheKey = {}", cacheKey);
    }

    @Override
    public RecipeEntity getByName(String name) {
        LOGGER.info("Unusable method");
        return null;
    }

    @Override
    public RecipeEntity getById(UUID id) {
        LOGGER.info("Check cache: id = {}", id.toString());
        RecipeEntity recipeFromCache = recipesCache.getIfPresent(id);

        if (recipeFromCache == null) {
            LOGGER.info("Empty cache: id = {}", id.toString());
            throw new NullPointerException();
        }

        LOGGER.info("Get cache: id = {}, recipe = {}", id.toString(), recipeFromCache);
        return recipeFromCache;
    }

    @Override
    public RecipeEntity add(CacheKey cacheKey, RecipeEntity entity) {
        recipesCache.put(cacheKey, entity);
        LOGGER.info("Add cache: recipe = {}", entity);
        return entity;
    }
}
