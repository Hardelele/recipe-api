package com.github.hardelele.ra.controllers;

import com.github.hardelele.ra.models.transfers.IngredientTransfer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class IngredientController {

    @GetMapping("/ingredients")
    public List<IngredientTransfer> getAllIngredients() {
        List<IngredientTransfer> ingredients = new ArrayList<>();
        ingredients.add(IngredientTransfer.builder().build());
        return ingredients;
    }
}
