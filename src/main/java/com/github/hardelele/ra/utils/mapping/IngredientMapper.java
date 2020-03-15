package com.github.hardelele.ra.utils.mapping;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import com.github.hardelele.ra.models.forms.IngredientForm;
import com.github.hardelele.ra.utils.enums.Status;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Component
public class IngredientMapper {

    private final Mapper mapper;

    @Autowired
    public IngredientMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public IngredientEntity toEntity(IngredientForm ingredientForm) {
        Date date = new Date();
        IngredientEntity entity = mapper.map(ingredientForm, IngredientEntity.class);
        entity.setId(UUID.randomUUID());
        entity.setTimestamp(new Timestamp(date.getTime()));
        entity.setStatus(Status.ACTIVE);
        return entity;
    }
}
