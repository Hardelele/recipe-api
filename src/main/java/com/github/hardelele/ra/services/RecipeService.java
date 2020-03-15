package com.github.hardelele.ra.services;

import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.services.cache.RecipeCacheService;
import com.github.hardelele.ra.services.database.RecipeDatabaseService;
import com.github.hardelele.ra.utils.cache.CacheKey;
import com.github.hardelele.ra.utils.mapping.RecipeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeDatabaseService recipeDatabaseService;

    private final RecipeCacheService recipeCacheService;

    private final IngredientService ingredientService;

    private final RecipeMapper recipeMapper;

    @Autowired
    public RecipeService(RecipeDatabaseService recipeDatabaseService,
                         RecipeCacheService recipeCacheService,
                         IngredientService ingredientService,
                         RecipeMapper recipeMapper) {
        this.recipeDatabaseService = recipeDatabaseService;
        this.recipeCacheService = recipeCacheService;
        this.ingredientService = ingredientService;
        this.recipeMapper = recipeMapper;
    }

    public RecipeEntity createRecipe(RecipeForm recipeForm) {
        Set<IngredientEntity> ingredients = getIngredients(recipeForm);
        RecipeEntity recipeToSave = recipeMapper.formToEntity(recipeForm, ingredients);
        return putInDatabaseAndCache(recipeToSave);
    }

    public List<RecipeEntity> getAllRecipes() {
        return recipeDatabaseService.getAllRecipes().stream()
                .map(recipeCacheService::putInCache)
                .collect(Collectors.toList());
    }

    public RecipeEntity getRecipe(UUID id) {
        try {
            return recipeCacheService.pullFormCacheById(id);
        } catch (NullPointerException ignored) {
            RecipeEntity recipeFromDatabase = recipeDatabaseService.pullFromDatabaseById(id);
            return recipeCacheService.putInCache(recipeFromDatabase);
        }
    }

    public RecipeEntity updateRecipe(UUID id, RecipeForm recipeForm) {
        RecipeEntity recipeEntity = getRecipe(id);
        Set<IngredientEntity> ingredients = getIngredients(recipeForm);
        RecipeEntity recipeToSave = recipeMapper.editEntity(recipeEntity, recipeForm, ingredients);
        return putInDatabaseAndCache(recipeToSave);
    }

    public void deleteRecipe(UUID id) {
        CacheKey cacheKey = recipeDatabaseService.deleteRecipe(id);
        recipeCacheService.deleteFromCache(cacheKey);
    }

    public void deleteAllRecipes() {
        recipeDatabaseService.deleteAllRecipes();
        recipeCacheService.cleanUp();
    }

    private RecipeEntity putInDatabaseAndCache(RecipeEntity recipeToSave) {
        return recipeCacheService.putInCache(recipeDatabaseService.putInDatabase(recipeToSave));
    }

    private Set<IngredientEntity> getIngredients(RecipeForm recipeForm) {
        return ingredientService.mapIngredientsToEntity(recipeForm);
    }
}
