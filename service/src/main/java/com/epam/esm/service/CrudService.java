package com.epam.esm.service;

import com.epam.esm.dto.EntityDTO;

public interface CrudService<T extends EntityDTO> {

    T create(T entity);

    T update(T entity);

    void delete(Long id);

    T findById(Long id);
}
