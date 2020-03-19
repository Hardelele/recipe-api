package com.github.hardelele.ra.services.database;

import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.repositories.RecipeRepository;
import com.github.hardelele.ra.services.IngredientService;
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
        recipeRepository.deleteAll();
    }

    @Override
    public void delete(UUID id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public RecipeEntity add(RecipeEntity entity) {
        return recipeRepository.save(entity);
    }

    @Override
    public RecipeEntity get(UUID id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("recipe by id:" + id, HttpStatus.NOT_FOUND));
    }
}
