package com.github.hardelele.ra.services;

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

    public List<IngredientTransfer> getAllIngredients() {

        return ingredientRepository.findAll().stream()
                .map(entity -> mapper.map(entity, IngredientTransfer.class))
                .collect(Collectors.toList());
    }

    public IngredientTransfer getIngredient(UUID id) {
        return ingredientRepository.findById(id)
                .map(entity -> mapper.map(entity, IngredientTransfer.class))
                .orElseThrow(() -> new NotFoundException("Not found ingredient by id:" + id, HttpStatus.NOT_FOUND));
    }

    public IngredientTransfer createIngredient(IngredientForm ingredientForm) {

        IngredientEntity ingredientToSave = IngredientEntity.builder()
                .id(UUID.randomUUID())
                .name(ingredientForm.getName())
                .status(Status.ACTIVE)
                .timestamp(new Timestamp(date.getTime()))
                .build();

        ingredientToSave = ingredientRepository.save(ingredientToSave);
        return mapper.map(ingredientToSave, IngredientTransfer.class);
    }

    public IngredientTransfer updateIngredient(UUID id, IngredientForm ingredientForm) {

        IngredientEntity entity = ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found ingredient by id:" + id, HttpStatus.NOT_FOUND));
        entity.setName(ingredientForm.getName());
        ingredientRepository.save(entity);
        return mapper.map(entity, IngredientTransfer.class);
    }

    public void deleteAllIngredients() {
        ingredientRepository.deleteAll();
    }

    public void deleteIngredient(UUID id) {
        ingredientRepository.deleteById(id);
    }
}
