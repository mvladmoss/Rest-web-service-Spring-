package com.epam.esm.dto;


import javax.validation.constraints.Digits;

public abstract class EntityDTO {

    @Digits(integer = 8, fraction = 0)
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
