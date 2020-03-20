package com.github.hardelele.ra.controllers;

import com.github.hardelele.ra.models.transfers.IngredientTransfer;
import com.github.hardelele.ra.models.transfers.RecipeTransfer;
import com.github.hardelele.ra.services.IngredientService;
import org.dozer.Mapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("/ingredients")
public class IngredientsController {

    private final IngredientService ingredientService;

    private final Mapper mapper;

    public IngredientsController(IngredientService ingredientService, Mapper mapper) {
        this.ingredientService = ingredientService;
        this.mapper = mapper;
    }

    @GetMapping("/recipeId/{id}")
    public List<IngredientTransfer> getAllRecipes(@PathVariable UUID id) {
        return ingredientService
                .getAllByRecipeId(id).stream()
                .map(entity -> mapper.map(entity, IngredientTransfer.class))
                .collect(Collectors.toList());
    }
}
