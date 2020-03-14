package com.github.hardelele.ra.services;

import com.github.hardelele.ra.exceptions.AlreadyExistException;
import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.models.transfers.IngredientTransfer;
import com.github.hardelele.ra.repositories.IngredientRepository;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    private final Mapper mapper;

    private final Date date = new Date();;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, Mapper mapper) {
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    public List<IngredientEntity> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public IngredientEntity getIngredientById(UUID id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found ingredient by id:" + id, HttpStatus.NOT_FOUND));
    }

    public IngredientEntity getIngredientByName(String name) {
        if(!isExistByName(name)) {
            throw new NotFoundException("Not found ingredient by id:" + name, HttpStatus.NOT_FOUND);
        }
        return ingredientRepository.findByName(name);
    }

    public IngredientEntity createIngredient(IngredientForm ingredientFromForm) {
        String name = ingredientFromForm.getName();
        if (isExistByName(name)) {
            throw new AlreadyExistException("Ingredient by name: " + name + " already exist", HttpStatus.NOT_FOUND);
        }
        return ingredientRepository.save(formToEntity(ingredientFromForm));
    }

    public IngredientEntity updateIngredient(UUID id, IngredientForm ingredientForm) {
        IngredientEntity entity = ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found ingredient by id:" + id, HttpStatus.NOT_FOUND));
        entity.setName(ingredientForm.getName());
        return ingredientRepository.save(entity);
    }

    public void deleteAllIngredients() {
        ingredientRepository.deleteAll();
    }

    public void deleteIngredient(UUID id) {
        ingredientRepository.deleteById(id);
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
