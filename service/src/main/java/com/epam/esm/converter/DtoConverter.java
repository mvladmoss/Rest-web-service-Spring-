package com.epam.esm.converter;

public interface DtoConverter<Entity, DtoEntity> {
    DtoEntity convert(Entity entity);

    Entity unconvert(DtoEntity dtoEntity);
}
