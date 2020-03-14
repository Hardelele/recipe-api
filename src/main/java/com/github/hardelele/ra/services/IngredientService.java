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

    private final IngredientRepository ingredientRepository;
    private final Mapper mapper;
    private final Date date = new Date();
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
        } catch (NullPointerException throwable) {
            IngredientEntity ingredientEntity = ingredientRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("ingredient by id:" + id, HttpStatus.NOT_FOUND));
            return putInCache(ingredientEntity);
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
            throw new AlreadyExistException("Ingredient by name: " + name, HttpStatus.NOT_FOUND);
        }
        return ingredientRepository.save(formToEntity(ingredientFromForm));
    }

    public IngredientEntity updateIngredient(UUID id, IngredientForm ingredientForm) {
        IngredientEntity entity = ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ingredient by id:" + id, HttpStatus.NOT_FOUND));
        entity.setName(ingredientForm.getName());
        return ingredientRepository.save(entity);
    }

    public void deleteAllIngredients() {
        ingredientRepository.deleteAll();
    }

    public void deleteIngredient(UUID id) {
        ingredientRepository.deleteById(id);
    }

    private IngredientEntity pullFormCacheById(UUID id) {
        IngredientEntity ingredientFromCache = ingredientsCache.getIfPresent(id);
        if (ingredientFromCache == null) {
            throw new NullPointerException();
        }
        return ingredientFromCache;
    }

    private IngredientEntity putInCache(IngredientEntity ingredientEntity) {
        CacheKey cacheKey = new CacheKey(ingredientEntity.getId(),ingredientEntity.getName());
        ingredientsCache.put(cacheKey, ingredientEntity);
        return ingredientEntity;
    }

    public IngredientEntity formToEntity(IngredientForm ingredientForm) {
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
