package com.epam.esm.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class CertificateSpecification implements SqlSpecification {

    private String sortBy;
    private String searchByName;
    private String searchByDescription;
    private String orderSort;
    private String tagId;
    private String tagName;

    public CertificateSpecification(String sortBy, String searchByDescription
            , String searchByName, String tagId, String tagName, String orderSort) {
        this.tagId = tagId;
        this.sortBy = sortBy;
        this.searchByDescription = searchByDescription;
        this.searchByName = searchByName;
        this.tagName = tagName;
        this.orderSort = orderSort;
    }

    private String getSqlDataFrom() {
        String sqlData = " gift_certificate";
        if (!StringUtils.isEmpty(searchByDescription)) {
            sqlData = " find_certificate_by_description(:searchByDescription)";
        }
        if (!StringUtils.isEmpty(searchByName)) {
            sqlData = " find_certificate_by_name(:searchByName)";
        }
        if (!StringUtils.isEmpty(searchByName) && !StringUtils.isEmpty(searchByDescription)) {
            sqlData = " find_certificate_by_name_and_description(:searchByName,:searchByDescription)";
        }
        return sqlData;
    }

    private StringBuilder getSortPart() {
        StringBuilder sortQuery = new StringBuilder();
        QueryValidator.checkSort(sortBy, orderSort);
        if (!StringUtils.isEmpty(sortBy)) {
            sortQuery.append(" ORDER BY ").append(sortBy);
            if (!StringUtils.isEmpty(orderSort)) {
                sortQuery.append(" ").append(orderSort);
            }
        }
        return sortQuery;
    }

    private StringBuilder getWherePart() {
        StringBuilder whereQuery = new StringBuilder();
        if (!StringUtils.isEmpty(tagId) || !StringUtils.isEmpty(tagName)) {
            String wherePart = " where ";
            whereQuery.append(wherePart);
            if (!StringUtils.isEmpty(tagId)) {
                wherePart = "tag.id = :tagId";
            }
            if (!StringUtils.isEmpty(tagName)) {
                wherePart = "tag.name ILIKE :tagName";
            }
            if (!StringUtils.isEmpty(tagId) && !StringUtils.isEmpty(tagName)) {
                wherePart = "tag.id = " + tagId + " AND " + "tag.name ILIKE :tagName";
            }
            whereQuery.append(wherePart);
        }
        return whereQuery;
    }

    private String queryBuilder() {

        String startQuery = "select DISTINCT gf_cert.id, gf_cert.name, gf_cert.description, gf_cert.price, gf_cert.date_of_creation, gf_cert.date_of_modification, gf_cert.duration_in_days\n" +
                " from ";
        StringBuilder finalQuery = new StringBuilder(startQuery);
        String findQuery = getSqlDataFrom();
        finalQuery.append(findQuery);
        String joinQuery = " gf_cert left join tag left join tag_gift_certificate tgc on tgc.tag_id=tag.id on gf_cert.id = tgc.gift_certificate_id";
        finalQuery.append(joinQuery);
        StringBuilder whereQuery = getWherePart();
        StringBuilder sortQuery = getSortPart();
        if (!whereQuery.toString().isEmpty()) {
            finalQuery.append(whereQuery);
        }

        if (!sortQuery.toString().isEmpty()) {
            finalQuery.append(sortQuery);
        }
        finalQuery.append(";");
        return finalQuery.toString();
    }

    @Override
    public String toSql() {
        return queryBuilder();
    }

    @Override
    public SqlParameterSource params() {
        Long tagIdLong = 0L;
        if (tagId != null) {
            tagIdLong = Long.valueOf(tagId);
        }
        return new MapSqlParameterSource()
                .addValue("tagId", tagIdLong)
                .addValue("searchByDescription", "%" + searchByDescription + "%")
                .addValue("searchByName", "%" + searchByName + "%")
                .addValue("sortBy", "gf_cert." + sortBy)
                .addValue("tagName", "%" + tagName + "%");

    }
}
