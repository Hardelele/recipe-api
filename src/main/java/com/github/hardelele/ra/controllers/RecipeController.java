package com.github.hardelele.ra.controllers;

import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.models.transfers.RecipeTransfer;
import com.github.hardelele.ra.services.RecipeService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    private final Mapper mapper;

    @Autowired
    public RecipeController(RecipeService recipeService, Mapper mapper) {
        this.recipeService = recipeService;
        this.mapper = mapper;
    }

    @PostMapping("/")
    public RecipeTransfer createRecipe(@RequestBody RecipeForm recipe) {
        return mapper.map(recipeService.createRecipe(recipe), RecipeTransfer.class);
    }

    @GetMapping("/")
    public List<RecipeTransfer> getAllRecipes() {
        return recipeService
                .getAllRecipes().stream()
                .map(entity -> mapper.map(entity, RecipeTransfer.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/id/{id}")
    public RecipeTransfer getRecipe(@PathVariable UUID id) {
        return mapper.map(recipeService.getRecipe(id), RecipeTransfer.class);
    }

    @PutMapping("/id/{id}")
    public RecipeTransfer editRecipe(@PathVariable UUID id, @RequestBody RecipeForm recipe) {
        return mapper.map(recipeService.updateRecipe(id, recipe), RecipeTransfer.class);
    }

    @DeleteMapping("/id/{id}")
    public void deleteRecipe(@PathVariable UUID id) {
        recipeService.deleteRecipe(id);
    }

    @DeleteMapping("/clearAll")
    public void clearDatabase() {
        recipeService.deleteAllRecipes();
    }
}
