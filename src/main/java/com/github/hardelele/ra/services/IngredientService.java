package com.github.hardelele.ra.services;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.services.database.IngredientDatabaseService;
import com.github.hardelele.ra.utils.mapping.IngredientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IngredientService {

    private final IngredientDatabaseService ingredientDatabaseService;

    private final IngredientMapper ingredientMapper;

    @Autowired
    public IngredientService(IngredientDatabaseService ingredientDatabaseService, IngredientMapper ingredientMapper) {
        this.ingredientDatabaseService = ingredientDatabaseService;
        this.ingredientMapper = ingredientMapper;
    }

    public List<IngredientEntity> getAllByRecipeId(UUID id) {
        return ingredientDatabaseService.getAllByRecipeId(id);
    }
}
