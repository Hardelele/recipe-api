package com.github.hardelele.ra.services.cache;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.services.IngredientService;
import com.github.hardelele.ra.utils.cache.CacheKey;
import com.github.hardelele.ra.utils.cache.DoubleKeyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecipeCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final DoubleKeyCache<RecipeEntity> recipesCache = new DoubleKeyCache<>();

    public void cleanUp() {
        recipesCache.cleanUp();
    }

    public void deleteFromCache(CacheKey cacheKey) {
        recipesCache.delete(cacheKey);
    }

    public RecipeEntity pullFormCacheByName(String name) {

        LOGGER.info("Pulling recipe entity form cache by name: {}", name);
        RecipeEntity recipeEntity = recipesCache.getIfPresent(name);

        if (recipeEntity == null) {
            LOGGER.info("Got empty cache by name: {}", name);
            throw new NullPointerException();
        }

        LOGGER.info("Got entity form cache by name: {}, cache = {}", name, recipeEntity);
        return recipeEntity;
    }

    public RecipeEntity pullFormCacheById(UUID id) {

        LOGGER.info("Pulling recipe entity form cache by id: {}", id.toString());
        RecipeEntity recipeFromCache = recipesCache.getIfPresent(id);

        if (recipeFromCache == null) {
            LOGGER.info("Got empty cache by id: {}", id.toString());
            throw new NullPointerException();
        }

        LOGGER.info("Got entity form cache by id: {}, cache = {}", id.toString(), recipeFromCache);
        return recipeFromCache;
    }

    public RecipeEntity putInCache(RecipeEntity recipeEntity) {
        CacheKey cacheKey = new CacheKey(recipeEntity.getId(),recipeEntity.getName());
        LOGGER.info("Putting recipe entity in cache: {}", recipeEntity);
        recipesCache.put(cacheKey, recipeEntity);
        return recipeEntity;
    }
}
