package com.github.hardelele.ra.services;

import com.github.hardelele.ra.exceptions.NotFoundException;
import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.entities.RecipeEntity;
import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.models.forms.RecipeForm;
import com.github.hardelele.ra.models.transfers.IngredientTransfer;
import com.github.hardelele.ra.models.transfers.RecipeTransfer;
import com.github.hardelele.ra.repositories.RecipeRepository;
import com.github.hardelele.ra.utils.enums.Status;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientService ingredientService;

    private final Mapper mapper;

    private final Date date = new Date();;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService, Mapper mapper) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.mapper = mapper;
    }

    public RecipeEntity createRecipe(RecipeForm recipeForm) {
        RecipeEntity recipeToSave = formToEntity(recipeForm);
        return recipeRepository.save(recipeToSave);
    }

    public List<RecipeEntity> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public RecipeEntity getRecipe(UUID id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found recipe by id:" + id, HttpStatus.NOT_FOUND));
    }

    public RecipeEntity updateRecipe(UUID id, RecipeForm recipeForm) {
        RecipeEntity recipeEntity = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found recipe by id:" + id, HttpStatus.NOT_FOUND));
        return recipeRepository.save(editEntity(recipeEntity, recipeForm));
    }

    public void deleteRecipe(UUID id) {
        recipeRepository.deleteById(id);
    }

    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }

    public RecipeEntity formToEntity(RecipeForm recipeForm) {
        RecipeEntity recipeToSave = mapper.map(recipeForm, RecipeEntity.class);
        recipeToSave.setIngredients(mapIngredientsToEntity(recipeForm));
        recipeToSave.setId(UUID.randomUUID());
        recipeToSave.setStatus(Status.ACTIVE);
        recipeToSave.setTimestamp(new Timestamp(date.getTime()));
        return recipeToSave;
    }

    private RecipeEntity editEntity(RecipeEntity recipeEntity, RecipeForm recipeForm) {
        recipeEntity.setName(recipeForm.getName());
        recipeEntity.setCookingMilliseconds(recipeForm.getCookingMilliseconds());
        recipeEntity.setDescription(recipeForm.getDescription());
        recipeEntity.setIngredients(mapIngredientsToEntity(recipeForm));
        return recipeEntity;
    }

    private Set<IngredientEntity> mapIngredientsToEntity(RecipeForm recipeForm) {
        return recipeForm.getIngredients().stream()
                .map(this::getOrCreateIngredient)
                .collect(Collectors.toSet());
    }

    private IngredientEntity getOrCreateIngredient(IngredientForm ingredientForm) {
        String name = ingredientForm.getName();
        if (!ingredientService.isExistByName(name)) {
            return ingredientService.createIngredient(ingredientForm);
        } else {
            return ingredientService.getIngredientByName(name);
        }
    }
}
