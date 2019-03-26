package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryImpl extends BasicCrudRepository<Tag> implements TagRepository {

    private static final String CREATE_SQL_TAG =
            "insert into tag (name) values (:name);";

    private static final String READ_SQL_TAG =
            "select id, name from tag where tag.id = :id";

    private static final String UPDATE_SQL_TAG =
            "update tag set name = :name where id = :id;";

    private static final String DELETE_SQL_TAG = "delete from tag where id = :id";

    private static final String DETACH_TAG_FROM_CERTIFICATE_SQL_BY_TAG_ID =
            "delete from tag_gift_certificate where tag_id = :tagId";

    @Autowired
    public TagRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    protected String getSqlQueryReadById() {
        return READ_SQL_TAG;
    }


    @Override
    protected String getSqlQueryCreate() {
        return CREATE_SQL_TAG;
    }

    @Override
    protected String getSqlQueryUpdate() {
        return UPDATE_SQL_TAG;
    }

    @Override
    protected String getSqlQueryDelete() {
        return DELETE_SQL_TAG;
    }

    @Override
    protected Class<Tag> getClassForQuery() {
        return Tag.class;
    }

    @Override
    public void detachTagFromCertificate(Long tagId) {
        namedParameterJdbcTemplate.update(DETACH_TAG_FROM_CERTIFICATE_SQL_BY_TAG_ID, new MapSqlParameterSource("tagId", tagId));
    }

    @Override
    protected SqlParameterSource getSqlParameterSource(Tag tag) {
        return new MapSqlParameterSource()
                .addValue("id", tag.getId())
                .addValue("name", tag.getName());
    }
}
