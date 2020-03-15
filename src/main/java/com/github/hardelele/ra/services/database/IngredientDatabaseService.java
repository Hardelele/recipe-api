package com.github.hardelele.ra.services.database;

import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.repositories.IngredientRepository;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredientDatabaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientDatabaseService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientEntity> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public void deleteAllIngredients() {
        ingredientRepository.findAll()
                .forEach(ingredientEntity -> {
                    UUID id = ingredientEntity.getId();
                    deleteIngredient(id);
                });
    }

    public CacheKey deleteIngredient(UUID id) {
        IngredientEntity ingredientEntity = pullFromDatabaseById(id);
        ingredientEntity.setStatus(Status.DELETED);
        return new CacheKey(ingredientEntity.getId(), ingredientEntity.getName());
    }

    public IngredientEntity putInDatabase(IngredientEntity ingredientToSave) {
        return ingredientRepository.save(ingredientToSave);
    }

    public IngredientEntity pullFromDatabaseByName(String name) {

        LOGGER.info("Pulling ingredient entity form database by name: {}", name);

        if(!isExistByName(name)) {
            LOGGER.info("Can not find nothing in database by name: {}", name);
            throw new NotFoundException("ingredient by name:" + name, HttpStatus.NOT_FOUND);
        }

        IngredientEntity ingredientEntity = ingredientRepository.findByName(name);
        LOGGER.info("Got entity from database by name: {}, cache = {}", name, ingredientEntity);
        return ingredientEntity;
    }

    public IngredientEntity pullFromDatabaseById(UUID id) {

        LOGGER.info("Pulling ingredient entity form database by id: {}", id.toString());
        IngredientEntity ingredientEntity = ingredientRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("Can not find nothing in database by id: {}", id.toString());
                    throw new NotFoundException("ingredient by id: " + id, HttpStatus.NOT_FOUND);
                });

        LOGGER.info("Got entity from database by id: {}, cache = {}", id.toString(), ingredientEntity);
        return ingredientEntity;
    }

    public boolean isExistByName(String name){
        return ingredientRepository.existsByName(name);
    }
}
