package com.epam.esm.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Tag extends Entity {

    private String name;

    public Tag() {
    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 41)
                .append(id)
                .append(name)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return new EqualsBuilder()
                .append(id, tag.id)
                .append(name, tag.name)
                .isEquals();
    }

    @Override
    public String toString() {
        return "Tag{" + "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
