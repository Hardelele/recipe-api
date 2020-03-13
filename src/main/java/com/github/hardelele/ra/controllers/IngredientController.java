package com.github.hardelele.ra.controllers;

import com.github.hardelele.ra.objects.forms.IngredientForm;
import com.github.hardelele.ra.objects.models.IngredientModel;
import com.github.hardelele.ra.objects.transfers.IngredientTransfer;
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
    public IngredientTransfer getIngredient(@RequestBody IngredientForm ingredient) {
        return ingredientService.createIngredient(ingredient);
    }
}
