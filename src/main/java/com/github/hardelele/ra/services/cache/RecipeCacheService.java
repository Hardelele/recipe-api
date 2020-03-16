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
    }

    @Override
    public void deleteByCacheKey(CacheKey cacheKey) {
        recipesCache.delete(cacheKey);
    }

    @Override
    public RecipeEntity getByName(String name) {
        return null;
    }

    @Override
    public RecipeEntity getById(UUID id) {
        RecipeEntity recipeFromCache = recipesCache.getIfPresent(id);

        if (recipeFromCache == null) {
            throw new NullPointerException();
        }

        return recipeFromCache;
    }

    @Override
    public RecipeEntity add(CacheKey cacheKey, RecipeEntity entity) {
        recipesCache.put(cacheKey, entity);
        return entity;
    }
}
