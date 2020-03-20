package com.github.hardelele.ra.services.database;

import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IngredientDatabaseService implements DatabaseService<IngredientEntity> {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientDatabaseService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<IngredientEntity> getAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public void cleanUp() {
        ingredientRepository.deleteAll();
    }

    @Override
    public void delete(UUID id) {
        ingredientRepository.deleteById(id);
    }

    @Override
    public IngredientEntity add(IngredientEntity entity) {
        return ingredientRepository.save(entity);
    }

    @Override
    public IngredientEntity get(UUID id) {
        return ingredientRepository.findById(id).orElseThrow(() ->
                new NotFoundException("ingredient by id: " + id, HttpStatus.NOT_FOUND));
    }

    public List<IngredientEntity> getAllByRecipeId(UUID id) {
        return ingredientRepository.findAllByRecipe_Id(id);
    }
}
