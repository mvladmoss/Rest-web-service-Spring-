package com.epam.esm.dto;

import com.epam.esm.validation.tag.UniqueTagName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class TagDto extends EntityDTO {

    @NotEmpty(message = "Name should't be empty")
    @Length(min = 2, max = 30)
    @UniqueTagName
    private String name;

    public TagDto() {
    }

    public TagDto(Long id, String name) {
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
        TagDto tagDto = (TagDto) o;
        return new EqualsBuilder()
                .append(id, tagDto.id)
                .append(name, tagDto.name)
                .isEquals();
    }

    @Override
    public String toString() {
        return "TagDto{" + "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
