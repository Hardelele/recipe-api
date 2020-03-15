package com.github.hardelele.ra.services;

import com.github.hardelele.ra.exceptions.AlreadyExistException;
import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.repositories.IngredientRepository;
import com.github.hardelele.ra.services.cache.DoubleKeyCache;
import com.github.hardelele.ra.services.cache.keys.CacheKey;
import com.github.hardelele.ra.utils.enums.Status;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IngredientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepository;

    private final Mapper mapper;

    private final DoubleKeyCache<IngredientEntity> ingredientsCache = new DoubleKeyCache<>();

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, Mapper mapper) {
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    public List<IngredientEntity> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public IngredientEntity getIngredientById(UUID id) {
        try {
            return pullFormCacheById(id);
        } catch (NullPointerException ignored) {
            return putInCache(pullFromDatabaseById(id));
        }
    }

    public IngredientEntity getIngredientByName(String name) {
        if(!isExistByName(name)) {
            throw new NotFoundException("ingredient by name:" + name, HttpStatus.NOT_FOUND);
        }
        return ingredientRepository.findByName(name);
    }

    public IngredientEntity createIngredient(IngredientForm ingredientFromForm) {
        String name = ingredientFromForm.getName();
        if (isExistByName(name)) {
            throw new AlreadyExistException("Ingredient by name: " + name, HttpStatus.BAD_REQUEST);
        }
        return ingredientRepository.save(formToEntity(ingredientFromForm));
    }

    public IngredientEntity updateIngredient(UUID id, IngredientForm ingredientForm) {
        IngredientEntity ingredientEntity;
        try {
            ingredientEntity = pullFormCacheById(id);
        } catch (NullPointerException ignored) {
            ingredientEntity = pullFromDatabaseById(id);
        }
        ingredientEntity.setName(ingredientForm.getName());
        return putInDatabase(ingredientEntity);
    }

    public void deleteAllIngredients() {
        ingredientRepository.deleteAll();
    }

    public void deleteIngredient(UUID id) {
        ingredientRepository.deleteById(id);
    }

    private IngredientEntity pullFromDatabaseById(UUID id) {

        LOGGER.info("Pulling ingredient entity form database by id: {}", id.toString());
        IngredientEntity ingredientEntity = ingredientRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("Can not find nothing in database by id: {}", id.toString());
                    throw new NotFoundException("ingredient by id: " + id, HttpStatus.NOT_FOUND);
                });

        LOGGER.info("Got entity from database by id: {}, cache = {}", id.toString(), ingredientEntity);
        return ingredientEntity;
    }

    private IngredientEntity pullFormCacheById(UUID id) {

        LOGGER.info("Pulling ingredient entity form cache by id: {}", id.toString());
        IngredientEntity ingredientFromCache = ingredientsCache.getIfPresent(id);

        if (ingredientFromCache == null) {
            LOGGER.info("Got empty cache by id: {}", id.toString());
            throw new NullPointerException();
        }

        LOGGER.info("Got entity form cache by id: {}, cache = {}", id.toString(), ingredientFromCache);
        return ingredientFromCache;
    }

    private IngredientEntity putInCache(IngredientEntity ingredientEntity) {
        CacheKey cacheKey = new CacheKey(ingredientEntity.getId(),ingredientEntity.getName());
        LOGGER.info("Putting ingredient entity in cache: {}", ingredientEntity);
        ingredientsCache.put(cacheKey, ingredientEntity);
        return ingredientEntity;
    }

    private IngredientEntity putInDatabase(IngredientEntity ingredientToSave) {
        ingredientRepository.save(ingredientToSave);
        return putInCache(ingredientToSave);
    }

    public IngredientEntity formToEntity(IngredientForm ingredientForm) {
        Date date = new Date();
        IngredientEntity entity = mapper.map(ingredientForm, IngredientEntity.class);
        entity.setId(UUID.randomUUID());
        entity.setTimestamp(new Timestamp(date.getTime()));
        entity.setStatus(Status.ACTIVE);
        return entity;
    }

    public boolean isExistByName(String name){
        return ingredientRepository.existsByName(name);
    }
}
