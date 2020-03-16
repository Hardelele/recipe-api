package com.github.hardelele.ra.services.database;

import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.repositories.IngredientRepository;
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
public class IngredientDatabaseService implements DatabaseService<IngredientEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

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
        ingredientRepository.findAll()
                .forEach(ingredientEntity -> {
                    UUID id = ingredientEntity.getId();
                    delete(id);
                });
    }

    @Override
    public CacheKey delete(UUID id) {
        IngredientEntity ingredientEntity = getById(id);
        ingredientRepository.deleteById(id);
        return new CacheKey(ingredientEntity.getId(), ingredientEntity.getName());
    }

    @Override
    public IngredientEntity add(IngredientEntity entity) {
        return ingredientRepository.save(entity);
    }

    @Override
    public IngredientEntity getByName(String name) {
        if(!isExistByName(name)) {
            throw new NotFoundException("ingredient by name:" + name, HttpStatus.NOT_FOUND);
        }
        IngredientEntity ingredientEntity = ingredientRepository.findByName(name);
        return ingredientEntity;
    }

    @Override
    public IngredientEntity getById(UUID id) {
        IngredientEntity ingredientEntity = ingredientRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("ingredient by id: " + id, HttpStatus.NOT_FOUND);
                });
        return ingredientEntity;
    }

    @Override
    public boolean isExistByName(String name){
        boolean isExistByName = ingredientRepository.existsByName(name);
        return isExistByName;
    }

    public List<IngredientEntity> getAllByRecipeId(UUID id) {
        return ingredientRepository.findAllByRecipe_Id(id);
    }
}
