package com.github.hardelele.ra.models.transfers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientTransfer {

    private String id;
    private String name;
    private long timestamp;
}
