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
        LOGGER.info("Get database: All ingredients");
        return ingredientRepository.findAll();
    }

    @Override
    public void cleanUp() {
        LOGGER.info("Delete all ingredients: ");
        ingredientRepository.findAll()
                .forEach(ingredientEntity -> {
                    UUID id = ingredientEntity.getId();
                    delete(id);
                });
        LOGGER.info("Clean up database");
    }

    @Override
    public CacheKey delete(UUID id) {
        IngredientEntity ingredientEntity = getById(id);
        ingredientRepository.deleteById(id);
        LOGGER.info("Delete database: ingredient = {}", ingredientEntity);
        return new CacheKey(ingredientEntity.getId(), ingredientEntity.getName());
    }

    @Override
    public IngredientEntity add(IngredientEntity entity) {
        LOGGER.info("Add database: ingredient = {}", entity);
        return ingredientRepository.save(entity);
    }

    @Override
    public IngredientEntity getByName(String name) {
        LOGGER.info("Check database: id: {}", name);
        if(!isExistByName(name)) {
            LOGGER.info("Empty database: id = {}", name);
            throw new NotFoundException("ingredient by name:" + name, HttpStatus.NOT_FOUND);
        }
        IngredientEntity ingredientEntity = ingredientRepository.findByName(name);
        LOGGER.info("Get database: id = {}, ingredient = {}", name, ingredientEntity);
        return ingredientEntity;
    }

    @Override
    public IngredientEntity getById(UUID id) {
        LOGGER.info("Check database: id: {}", id.toString());
        IngredientEntity ingredientEntity = ingredientRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("Empty database: id = {}", id.toString());
                    throw new NotFoundException("ingredient by id: " + id, HttpStatus.NOT_FOUND);
                });
        LOGGER.info("Get database: id = {}, cache = {}", id.toString(), ingredientEntity);
        return ingredientEntity;
    }

    @Override
    public boolean isExistByName(String name){
        boolean isExistByName = ingredientRepository.existsByName(name);
        LOGGER.info("Check database: name = {}, isExist = {}", name, isExistByName);
        return isExistByName;
    }
}
