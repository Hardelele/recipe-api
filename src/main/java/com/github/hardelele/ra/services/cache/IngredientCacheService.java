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
public class IngredientCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final DoubleKeyCache<IngredientEntity> ingredientsCache = new DoubleKeyCache<>();

    public void cleanUp() {
        ingredientsCache.cleanUp();
    }

    public void deleteFromCache(CacheKey cacheKey) {
        ingredientsCache.delete(cacheKey);
    }

    public IngredientEntity pullFormCacheByName(String name) {

        LOGGER.info("Pulling ingredient entity form cache by name: {}", name);
        IngredientEntity ingredientFromCache = ingredientsCache.getIfPresent(name);

        if (ingredientFromCache == null) {
            LOGGER.info("Got empty cache by name: {}", name);
            throw new NullPointerException();
        }

        LOGGER.info("Got entity form cache by name: {}, cache = {}", name, ingredientFromCache);
        return ingredientFromCache;
    }

    public IngredientEntity pullFormCacheById(UUID id) {

        LOGGER.info("Pulling ingredient entity form cache by id: {}", id.toString());
        IngredientEntity ingredientFromCache = ingredientsCache.getIfPresent(id);

        if (ingredientFromCache == null) {
            LOGGER.info("Got empty cache by id: {}", id.toString());
            throw new NullPointerException();
        }

        LOGGER.info("Got entity form cache by id: {}, cache = {}", id.toString(), ingredientFromCache);
        return ingredientFromCache;
    }

    public IngredientEntity putInCache(IngredientEntity ingredientEntity) {
        CacheKey cacheKey = new CacheKey(ingredientEntity.getId(),ingredientEntity.getName());
        LOGGER.info("Putting ingredient entity in cache: {}", ingredientEntity);
        ingredientsCache.put(cacheKey, ingredientEntity);
        return ingredientEntity;
    }


}
