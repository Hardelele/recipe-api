package com.github.hardelele.ra.utils.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CacheKey {

    private UUID id;
    private String name;
}
