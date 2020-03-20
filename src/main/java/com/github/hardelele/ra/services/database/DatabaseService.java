package com.github.hardelele.ra.services.database;

import java.util.List;
import java.util.UUID;

public interface DatabaseService<E> {

    List<E> getAll();
    void cleanUp();
    void delete(UUID id);
    E add(E entity);
    E get(UUID id);
}
