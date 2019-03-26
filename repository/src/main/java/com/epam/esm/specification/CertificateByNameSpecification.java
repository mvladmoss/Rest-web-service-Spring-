package com.epam.esm.specification;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class CertificateByNameSpecification implements SqlSpecification {

    private String name;

    public CertificateByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public String toSql() {
        return "select id, name, description, price, date_of_creation,date_of_modification,duration_in_days from gift_certificate where name = :name;";
    }

    @Override
    public SqlParameterSource params() {
        return new MapSqlParameterSource().addValue("name", name);
    }
}
