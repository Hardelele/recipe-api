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
        return recipeRepository.findAll();
    }

    @Override
    public void cleanUp() {
        recipeRepository.findAll()
                .forEach(recipeEntity -> {
                    UUID id = recipeEntity.getId();
                    delete(id);
                });
    }

    @Override
    public CacheKey delete(UUID id) {
        RecipeEntity recipeEntity = getById(id);
        recipeRepository.deleteById(id);
        return new CacheKey(recipeEntity.getId(), recipeEntity.getName());
    }

    @Override
    public RecipeEntity add(RecipeEntity entity) {
        return recipeRepository.save(entity);
    }

    @Override
    public RecipeEntity getByName(String name) {
        return null;
    }

    @Override
    public RecipeEntity getById(UUID id) {
        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("recipe by id:" + id, HttpStatus.NOT_FOUND);
                });

        return recipeEntity;
    }

    @Override
    public boolean isExistByName(String name) {
        return false;
    }
}
