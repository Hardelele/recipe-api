package com.github.hardelele.ra.services.database;

import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.repositories.RecipeRepository;
import com.github.hardelele.ra.services.IngredientService;
import com.github.hardelele.ra.utils.cache.CacheKey;
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
public class RecipeDatabaseService implements DatabaseService<RecipeEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeDatabaseService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<RecipeEntity> getAll() {
        LOGGER.info("Get database: All recipes");
        return recipeRepository.findAll();
    }

    @Override
    public void cleanUp() {
        LOGGER.info("Delete all recipes: ");
        recipeRepository.findAll()
                .forEach(recipeEntity -> {
                    UUID id = recipeEntity.getId();
                    delete(id);
                });
        LOGGER.info("Clean up database");
    }

    @Override
    public CacheKey delete(UUID id) {
        RecipeEntity recipeEntity = getById(id);
        recipeRepository.deleteById(id);
        LOGGER.info("Delete database: recipe = {}", recipeEntity);
        return new CacheKey(recipeEntity.getId(), recipeEntity.getName());
    }

    @Override
    public RecipeEntity add(RecipeEntity entity) {
        LOGGER.info("Add database: recipe = {}", entity);
        return recipeRepository.save(entity);
    }

    @Override
    public RecipeEntity getByName(String name) {
        LOGGER.info("Unusable method");
        return null;
    }

    @Override
    public RecipeEntity getById(UUID id) {
        LOGGER.info("Check database: id = {}", id.toString());
        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("Empty database: id = {}", id.toString());
                    throw new NotFoundException("recipe by id:" + id, HttpStatus.NOT_FOUND);
                });

        LOGGER.info("Get database: id = {}, recipe = {}", id.toString(), recipeEntity);
        return recipeEntity;
    }

    @Override
    public boolean isExistByName(String name) {
        LOGGER.info("Unusable method");
        return false;
    }
}
