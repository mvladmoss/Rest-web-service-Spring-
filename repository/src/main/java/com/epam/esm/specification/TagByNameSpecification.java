package com.epam.esm.specification;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class TagByNameSpecification implements SqlSpecification {

    private String name;

    public TagByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public String toSql() {
        return "select id, name from tag where name = :name;";
    }

    @Override
    public SqlParameterSource params() {
        return new MapSqlParameterSource("name", name);
    }
}
