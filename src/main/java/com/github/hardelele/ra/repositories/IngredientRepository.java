package com.github.hardelele.ra.repositories;

import com.github.hardelele.ra.models.entities.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, UUID> {
    List<IngredientEntity> findAllByRecipe_Id(UUID id);
}
