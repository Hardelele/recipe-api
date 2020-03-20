package com.github.hardelele.ra.services;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.services.cache.CacheService;
import com.github.hardelele.ra.services.database.RecipeDatabaseService;
import com.github.hardelele.ra.utils.mapping.RecipeMapper;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RecipeService {

    private final RecipeDatabaseService recipeDatabaseService;

    private final CacheService<RecipeEntity> cacheService;

    private final RecipeMapper recipeMapper;

    private final Mapper mapper;


    @Autowired
    public RecipeService(RecipeDatabaseService recipeDatabaseService,
                         CacheService<RecipeEntity> cacheService,
                         RecipeMapper recipeMapper,
                         Mapper mapper) {
        this.recipeDatabaseService = recipeDatabaseService;
        this.cacheService = cacheService;
        this.recipeMapper = recipeMapper;
        this.mapper = mapper;
    }

    public RecipeEntity createRecipe(RecipeForm recipeForm) {

        final List<IngredientEntity> ingredientEntityList = extractIngredientEntityList(recipeForm);
        RecipeEntity recipeEntity = recipeMapper.formToEntity(recipeForm, ingredientEntityList);
        recipeEntity.getIngredients().forEach(ingredientEntity -> ingredientEntity.setRecipe(recipeEntity));

        return putInDatabaseAndCache(recipeEntity);
    }

    public List<RecipeEntity> getAllRecipes() {
        return recipeDatabaseService.getAll();
    }

    public RecipeEntity getRecipe(UUID id) {
        RecipeEntity recipeEntity = cacheService.get(id, RecipeEntity.class);
        if (recipeEntity != null) {
            log.info("Received cache: {}", recipeEntity);
            return recipeEntity;
        } else {
            recipeEntity = recipeDatabaseService.get(id);
            return cacheService.add(id, recipeEntity);
        }
    }

    public RecipeEntity updateRecipe(UUID id, RecipeForm recipeForm) {
        final RecipeEntity recipeEntity = getRecipe(id);
        final List<IngredientEntity> ingredientEntityList = extractIngredientEntityList(recipeForm);
        RecipeEntity recipeToSave = recipeMapper.editEntity(recipeEntity, recipeForm, ingredientEntityList);
        recipeToSave.getIngredients().forEach(ingredientEntity -> ingredientEntity.setRecipe(recipeEntity));
        return putInDatabaseAndCache(recipeToSave);
    }

    public void deleteRecipe(UUID id) {
        recipeDatabaseService.delete(id);
        cacheService.delete(id, RecipeEntity.class);
    }

    public void deleteAllRecipes() {
        recipeDatabaseService.cleanUp();
        cacheService.cleanUp();
    }

    private RecipeEntity putInDatabaseAndCache(RecipeEntity recipeToSave) {
        final UUID recipeId = recipeToSave.getId();
        final RecipeEntity recipeFromDatabase = recipeDatabaseService.add(recipeToSave);
        return cacheService.add(recipeId, recipeFromDatabase);
    }

    private List<IngredientEntity> extractIngredientEntityList(RecipeForm recipeForm) {
        final List<IngredientForm> ingredientFormList = recipeForm.getIngredients();
        return ingredientFormList.stream()
                .map(ingredientForm -> mapper.map(ingredientForm, IngredientEntity.class))
                .collect(Collectors.toList());
    }
}
