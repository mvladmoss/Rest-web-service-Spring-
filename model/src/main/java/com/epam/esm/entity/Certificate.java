package com.epam.esm.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Certificate extends Entity {

    private String name;
    private String description;
    private int durationInDays;
    private BigDecimal price;
    private Instant dateOfModification;
    private Instant dateOfCreation;
    private Set<Tag> tags = new HashSet<>();

    public Certificate() {
    }

    public Certificate(Long id, String name, String description, int durationInDays, BigDecimal price
            , Instant dateOfModification, Instant dateOfCreation, Set<Tag> tags) {
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return durationInDays == that.durationInDays &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                price.compareTo(that.price) == 0 &&
                Objects.equals(dateOfModification, that.dateOfModification) &&
                Objects.equals(dateOfCreation, that.dateOfCreation) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, description, durationInDays, price, dateOfModification, dateOfCreation, tags);
    }

    @Override
    public String toString() {
        return "Certificate{" +
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
