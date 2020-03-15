package com.github.hardelele.ra.services.database;

import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.repositories.RecipeRepository;
import com.github.hardelele.ra.services.IngredientService;
import com.github.hardelele.ra.utils.cache.CacheKey;
import com.github.hardelele.ra.utils.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RecipeDatabaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeDatabaseService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeEntity> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public void deleteAllRecipes() {
        recipeRepository.findAll()
                .forEach(recipeEntity -> {
                    UUID id = recipeEntity.getId();
                    deleteRecipe(id);
                });
    }

    public CacheKey deleteRecipe(UUID id) {
        RecipeEntity recipeEntity = pullFromDatabaseById(id);
        recipeEntity.setStatus(Status.DELETED);
        return new CacheKey(recipeEntity.getId(), recipeEntity.getName());
    }

    public RecipeEntity putInDatabase(RecipeEntity recipeToSave) {
        return recipeRepository.save(recipeToSave);
    }

    public RecipeEntity pullFromDatabaseById(UUID id) {

        LOGGER.info("Pulling recipe entity form database by id: {}", id.toString());
        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("Can not find recipe in database by id: {}", id.toString());
                    throw new NotFoundException("recipe by id:" + id, HttpStatus.NOT_FOUND);
                });

        LOGGER.info("Got recipe from database by id: {}, recipe = {}", id.toString(), recipeEntity);
        return recipeEntity;
    }
}
