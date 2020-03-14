package com.github.hardelele.ra.controllers;

import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.models.transfers.IngredientTransfer;
import com.github.hardelele.ra.services.IngredientService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    private final Mapper mapper;

    @Autowired
    public IngredientController(IngredientService ingredientService, Mapper mapper) {
        this.ingredientService = ingredientService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public List<IngredientTransfer> getAllIngredients() {
        return ingredientService
                .getAllIngredients().stream()
                .map(entity -> mapper.map(entity, IngredientTransfer.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/id/{id}")
    public IngredientTransfer getIngredient(@PathVariable UUID id) {
        return mapper.map(ingredientService.getIngredientById(id), IngredientTransfer.class);
    }

    @GetMapping("/name/{name}")
    public IngredientTransfer getIngredient(@PathVariable String name) {
        return mapper.map(ingredientService.getIngredientByName(name), IngredientTransfer.class);
    }

    @PostMapping("/")
    public IngredientTransfer createIngredient(@RequestBody IngredientForm ingredient) {
        return mapper.map(ingredientService.createIngredient(ingredient), IngredientTransfer.class);
    }

    @PutMapping("/id/{id}")
    public IngredientTransfer editIngredient(@RequestBody IngredientForm ingredient, @PathVariable UUID id) {
        return mapper.map(ingredientService.updateIngredient(id, ingredient), IngredientTransfer.class);
    }

    @DeleteMapping("/id/{id}")
    public void deleteIngredient(@PathVariable UUID id) {
        ingredientService.deleteIngredient(id);
    }

    @DeleteMapping("/clearAll")
    public void clearDatabase() {
        ingredientService.deleteAllIngredients();
    }
}
