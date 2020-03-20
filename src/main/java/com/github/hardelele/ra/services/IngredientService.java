package com.github.hardelele.ra.services;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.services.database.IngredientDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IngredientService {

    private final IngredientDatabaseService ingredientDatabaseService;

    @Autowired
    public IngredientService(IngredientDatabaseService ingredientDatabaseService) {
        this.ingredientDatabaseService = ingredientDatabaseService;
    }

    public List<IngredientEntity> getAllByRecipeId(UUID id) {
        return ingredientDatabaseService.getAllByRecipeId(id);
    }
}
