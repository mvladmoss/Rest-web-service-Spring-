package com.epam.esm.repository;

import com.epam.esm.entity.Entity;
import com.epam.esm.specification.SqlSpecification;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends Entity> {
    Long create(T entity);

    Optional<T> findById(Long id);

    boolean update(T entity);

    boolean delete(Long id);

    List<T> query(SqlSpecification specification);

}
