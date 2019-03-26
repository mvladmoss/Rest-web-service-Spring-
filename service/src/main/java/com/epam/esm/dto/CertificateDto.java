package com.epam.esm.dto;

import com.epam.esm.validation.certificate.UniqueCertificateName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class CertificateDto extends EntityDTO {

    @UniqueCertificateName
    @NotEmpty(message = "name.not.empty")
    @Length(min = 2, max = 30,message = "name.length")
    private String name;
    @NotEmpty
    @Length(min = 5, max = 500,message = "Minimum length=5 and maximum=500")
    private String description;
    @Min(value = 1,message = "Minimum days = 1")
    private int durationInDays;
    @Digits(integer = 8, fraction = 3)
    @Min(value = 0,message = "Minimum price = 0")
    private BigDecimal price;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant dateOfModification;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant dateOfCreation;
    private Set<TagDto> tags = new HashSet<>();

    public CertificateDto() {
    }

    public CertificateDto(Long id, String name, String description, int durationInDays, BigDecimal price
            , Instant dateOfModification, Instant dateOfCreation, Set<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationInDays = durationInDays;
        this.price = price;
        this.dateOfModification = dateOfModification;
        this.dateOfCreation = dateOfCreation;
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getDateOfModification() {
        return dateOfModification;
    }

    public void setDateOfModification(Instant dateOfModification) {
        this.dateOfModification = dateOfModification;
    }

    public Instant getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Instant dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CertificateDto certificate = (CertificateDto) o;
        return new EqualsBuilder()
                .append(id, certificate.id)
                .append(name, certificate.name)
                .append(price, certificate.price)
                .append(tags, certificate.tags)
                .append(description, certificate.description)
                .append(durationInDays, certificate.durationInDays)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 41)
                .append(id)
                .append(name)
                .append(description)
                .append(dateOfCreation)
                .append(tags)
                .append(dateOfModification)
                .append(durationInDays)
                .append(price)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "CertificateDto{" +
                "description='" + description + '\'' +
                ", price=" + price +
                ", dateOfCreation=" + dateOfCreation +
                ", dateOfModification=" + dateOfModification +
                ", durationInDays=" + durationInDays +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", id=" + id +
                '}';
    }
}
