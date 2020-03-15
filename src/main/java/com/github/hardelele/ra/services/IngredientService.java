package com.github.hardelele.ra.services;

import com.github.hardelele.ra.exceptions.AlreadyExistException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.services.cache.IngredientCacheService;
import com.github.hardelele.ra.services.database.IngredientDatabaseService;
import com.github.hardelele.ra.utils.cache.CacheKey;
import com.github.hardelele.ra.utils.mapping.IngredientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientCacheService ingredientCacheService;

    private final IngredientDatabaseService ingredientDatabaseService;

    private final IngredientMapper ingredientMapper;

    @Autowired
    public IngredientService(IngredientCacheService ingredientCacheService,
                             IngredientDatabaseService ingredientDatabaseService,
                             IngredientMapper ingredientMapper) {
        this.ingredientCacheService = ingredientCacheService;
        this.ingredientDatabaseService = ingredientDatabaseService;
        this.ingredientMapper = ingredientMapper;
    }

    public List<IngredientEntity> getAllIngredients() {
        return ingredientDatabaseService.getAllIngredients().stream()
                .map(ingredientCacheService::putInCache)
                .collect(Collectors.toList());
    }

    public IngredientEntity getIngredientById(UUID id) {
        try {
            return ingredientCacheService.pullFormCacheById(id);
        } catch (NullPointerException ignored) {
            IngredientEntity ingredientFromDatabase = ingredientDatabaseService.pullFromDatabaseById(id);
            return ingredientCacheService.putInCache(ingredientFromDatabase);
        }
    }

    public IngredientEntity getIngredientByName(String name) {
        try {
            return ingredientCacheService.pullFormCacheByName(name);
        } catch (NullPointerException ignored) {
            IngredientEntity ingredientFromDatabase = ingredientDatabaseService.pullFromDatabaseByName(name);
            return ingredientCacheService.putInCache(ingredientFromDatabase);
        }
    }

    public IngredientEntity createIngredient(IngredientForm ingredientFromForm) {
        String name = ingredientFromForm.getName();
        if (isExistByName(name)) {
            throw new AlreadyExistException("Ingredient by name: " + name, HttpStatus.BAD_REQUEST);
        }
        return putInDatabaseAndCache(ingredientMapper.toEntity(ingredientFromForm));
    }

    public IngredientEntity updateIngredient(UUID id, IngredientForm ingredientForm) {
        IngredientEntity ingredientEntity = getIngredientById(id);
        ingredientEntity.setName(ingredientForm.getName());
        return putInDatabaseAndCache(ingredientEntity);
    }

    public void deleteIngredient(UUID id) {
        CacheKey cacheKey = ingredientDatabaseService.deleteIngredient(id);
        ingredientCacheService.deleteFromCache(cacheKey);
    }

    public void deleteAllIngredients() {
        ingredientDatabaseService.deleteAllIngredients();
        ingredientCacheService.cleanUp();
    }

    private IngredientEntity putInDatabaseAndCache(IngredientEntity ingredientToSave) {
        return ingredientCacheService.putInCache(ingredientDatabaseService.putInDatabase(ingredientToSave));
    }

    public boolean isExistByName(String name) {
        return ingredientDatabaseService.isExistByName(name);
    }

    public Set<IngredientEntity> mapIngredientsToEntity(RecipeForm recipeForm) {
        return recipeForm.getIngredients().stream()
                .map(this::getOrCreateIngredient)
                .collect(Collectors.toSet());
    }

    private IngredientEntity getOrCreateIngredient(IngredientForm ingredientForm) {
        String name = ingredientForm.getName();
        if (!isExistByName(name)) {
            return createIngredient(ingredientForm);
        } else {
            return getIngredientByName(name);
        }
    }
}
