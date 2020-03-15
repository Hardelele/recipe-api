package com.github.hardelele.ra.utils.mapping;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.utils.enums.Status;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class RecipeMapper {

    private final Mapper mapper;

    @Autowired
    public RecipeMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public RecipeEntity formToEntity(RecipeForm recipeForm, Set<IngredientEntity> ingredients) {
        Date date = new Date();
        RecipeEntity recipeToSave = mapper.map(recipeForm, RecipeEntity.class);
        recipeToSave.setIngredients(ingredients);
        recipeToSave.setId(UUID.randomUUID());
        recipeToSave.setStatus(Status.ACTIVE);
        recipeToSave.setTimestamp(new Timestamp(date.getTime()));
        return recipeToSave;
    }

    public RecipeEntity editEntity(RecipeEntity recipeEntity,
                                    RecipeForm recipeForm,
                                    Set<IngredientEntity> ingredients) {
        recipeEntity.setName(recipeForm.getName());
        recipeEntity.setCookingMilliseconds(recipeForm.getCookingMilliseconds());
        recipeEntity.setDescription(recipeForm.getDescription());
        recipeEntity.setIngredients(ingredients);
        return recipeEntity;
    }
}
