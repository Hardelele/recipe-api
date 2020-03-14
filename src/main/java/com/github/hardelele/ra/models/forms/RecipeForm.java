package com.github.hardelele.ra.models.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeForm {

    private String name;

    private String description;

    @JsonProperty(value = "cooking_in_milliseconds")
    private long cookingMilliseconds;

    private Set<IngredientForm> ingredients;
}
