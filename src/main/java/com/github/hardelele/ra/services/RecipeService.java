package com.github.hardelele.ra.services;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.models.transfers.RecipeTransfer;
import com.github.hardelele.ra.services.cache.RecipeCacheService;
import com.github.hardelele.ra.services.database.RecipeDatabaseService;
import com.github.hardelele.ra.utils.cache.CacheKey;
import com.github.hardelele.ra.utils.mapping.RecipeMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeService {

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
        List<IngredientEntity> ingredients = getIngredients(recipeForm);
        RecipeEntity recipeEntity = recipeMapper.formToEntity(recipeForm, ingredients);
        for (IngredientEntity ingredientEntity : recipeEntity.getIngredients()) {
            ingredientEntity.setRecipe(recipeEntity);
        }
        return putInDatabaseAndCache(recipeEntity);
    }

    public List<RecipeEntity> getAllRecipes() {
        return recipeDatabaseService.getAll().stream().map(recipeToSave -> {
            CacheKey cacheKey = getCacheKey(recipeToSave);
            return recipeCacheService.add(cacheKey, recipeToSave);
        }).collect(Collectors.toList());
    }

    public RecipeEntity getRecipe(UUID id) {
        RecipeEntity recipeFromDatabase;
        try {
            recipeFromDatabase = recipeCacheService.getById(id);
        } catch (NullPointerException ignored) {
            recipeFromDatabase = recipeDatabaseService.getById(id);
            CacheKey cacheKey = getCacheKey(recipeFromDatabase);
            recipeCacheService.add(cacheKey, recipeFromDatabase);
        }
        recipeFromDatabase.setIngredients(ingredientService.getAllByRecipeId(recipeFromDatabase.getId()));
        return recipeFromDatabase;
    }

    public RecipeEntity updateRecipe(UUID id, RecipeForm recipeForm) {
        RecipeEntity recipeEntity = getRecipe(id);
        List<IngredientEntity> ingredients = getIngredients(recipeForm);
        RecipeEntity recipeToSave = recipeMapper.editEntity(recipeEntity, recipeForm, ingredients);
        return putInDatabaseAndCache(recipeToSave);
    }

    public void deleteRecipe(UUID id) {
        CacheKey cacheKey = recipeDatabaseService.delete(id);
        recipeCacheService.deleteByCacheKey(cacheKey);
    }

    public void deleteAllRecipes() {
        recipeDatabaseService.cleanUp();
        recipeCacheService.cleanUp();
    }

    private CacheKey getCacheKey(RecipeEntity recipe) {
        return new CacheKey(recipe.getId(),recipe.getName());
    }

    private RecipeEntity putInDatabaseAndCache(RecipeEntity recipeToSave) {
        CacheKey cacheKey = getCacheKey(recipeToSave);
        recipeToSave = recipeDatabaseService.add(recipeToSave);
        return recipeCacheService.add(cacheKey, recipeToSave);
    }

    private List<IngredientEntity> getIngredients(RecipeForm recipeForm) {
        return ingredientService.mapIngredientsToEntity(recipeForm);
    }
}
