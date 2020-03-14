package com.github.hardelele.ra.models.entities;

import com.github.hardelele.ra.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ingredients")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name="recipe_id", nullable=false)
    private RecipeEntity recipe;
}
