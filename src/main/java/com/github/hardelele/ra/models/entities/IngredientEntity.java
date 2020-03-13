package com.github.hardelele.ra.models.entities;

import com.github.hardelele.ra.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ingredients")
public class IngredientEntity {

    @Id
    private UUID id;
    private String name;
    private Status status;
    private long timestamp;
}
