package com.github.hardelele.ra.controllers;

import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.models.transfers.IngredientTransfer;
import com.github.hardelele.ra.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredients")
    public List<IngredientTransfer> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/ingredients/{id}")
    public IngredientTransfer getIngredient(@PathVariable UUID id) {
        return ingredientService.getIngredient(id);
    }

    @PostMapping("/ingredients")
    public IngredientTransfer createIngredient(@RequestBody IngredientForm ingredient) {
        return ingredientService.createIngredient(ingredient);
    }

    @PutMapping("/ingredients/{id}")
    public IngredientTransfer editIngredient(@RequestBody IngredientForm ingredient, @PathVariable UUID id) {
        return ingredientService.updateIngredient(id, ingredient);
    }

    @DeleteMapping("/ingredients/{id}")
    public void deleteIngredient(@PathVariable UUID id) {
        ingredientService.deleteIngredient(id);
    }

    @DeleteMapping("/ingredients/clearAll")
    public void clearDatabase() {
        ingredientService.deleteAllIngredients();
    }
}
