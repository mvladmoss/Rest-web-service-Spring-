package com.epam.esm.specification;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class TagByCertificateId implements SqlSpecification {

    private Long id;

    public TagByCertificateId(Long id) {
        this.id = id;
    }

    @Override
    public String toSql() {
        return "select id, name from tag " +
                "join tag_gift_certificate tgc on tgc.tag_id=tag.id " +
                "where tgc.gift_certificate_id = :id";
    }

    @Override
    public SqlParameterSource params() {
        return new MapSqlParameterSource()
                .addValue("id", id);
    }
}
