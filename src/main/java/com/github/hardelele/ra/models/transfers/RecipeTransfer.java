package com.github.hardelele.ra.models.transfers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeTransfer {

    private String id;

    private String name;

    private String description;

    @JsonProperty(value = "cooking_in_milliseconds")
    private long cookingMilliseconds;

    private List<IngredientTransfer> ingredients;

    private Timestamp timestamp;
}
