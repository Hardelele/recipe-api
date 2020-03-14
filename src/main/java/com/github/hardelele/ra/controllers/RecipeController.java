package com.github.hardelele.ra.controllers;

import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.models.transfers.IngredientTransfer;
import com.github.hardelele.ra.models.transfers.RecipeTransfer;
import com.github.hardelele.ra.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/")
    public RecipeTransfer createRecipe(@RequestBody RecipeForm recipe) {
        return recipeService.createRecipe(recipe);
    }

    @GetMapping("/")
    public List<RecipeTransfer> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public RecipeTransfer getRecipe(@PathVariable UUID id) {
        return recipeService.getRecipe(id);
    }

    @PutMapping("/{id}")
    public RecipeTransfer editRecipe(@PathVariable UUID id, @RequestBody RecipeForm recipe) {
        return recipeService.updateRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable UUID id) {
        recipeService.deleteRecipe(id);
    }

    @DeleteMapping("/clearAll")
    public void clearDatabase() {
        recipeService.deleteAllRecipes();
    }
}
