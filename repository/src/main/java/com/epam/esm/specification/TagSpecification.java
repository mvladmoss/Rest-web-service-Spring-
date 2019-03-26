package com.epam.esm.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class TagSpecification implements SqlSpecification {

    private String findByName;
    private String sortOrder;
    private String sortBy;

    public TagSpecification(String sortBy, String findByName, String sortOrder) {
        this.findByName = findByName;
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
    }

    private StringBuilder returnFindPartOfQuery(String name) {
        StringBuilder builder = new StringBuilder();
        if (!StringUtils.isEmpty(name)) {
            builder.append(" where ");
            if (!StringUtils.isEmpty(name)) {
                builder.append(" name ILIKE :name");
            }
        }
        return builder;
    }

    private StringBuilder returnSortPart(String sortBy, String sortOrder) {
        StringBuilder builder = new StringBuilder();
        QueryValidator.checkSort(sortBy, sortOrder);
        if (!StringUtils.isEmpty(sortBy)) {
            builder.append(" ORDER BY tag.").append(sortBy);
            if (!StringUtils.isEmpty(sortOrder)) {
                builder.append(" ").append(sortOrder);
            }
        }
        return builder;
    }

    @Override
    public String toSql() {
        String startQuery = "select id, name from tag";
        StringBuilder findQuery = returnFindPartOfQuery(findByName);
        StringBuilder finalQuery = new StringBuilder(startQuery);
        finalQuery.append(findQuery);
        StringBuilder sortQuery = returnSortPart(sortBy, sortOrder);
        if (!sortQuery.toString().isEmpty()) {
            finalQuery.append(sortQuery);
        }
        finalQuery.append(";");
        return finalQuery.toString();
    }

    @Override
    public SqlParameterSource params() {
        return new MapSqlParameterSource()
                .addValue("name", "%" + findByName + "%");
    }
}
