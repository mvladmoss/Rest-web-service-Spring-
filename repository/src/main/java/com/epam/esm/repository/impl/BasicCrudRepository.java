package com.epam.esm.repository.impl;

import com.epam.esm.entity.Entity;
import com.epam.esm.specification.SqlSpecification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

public abstract class BasicCrudRepository<T extends Entity> {

    private final RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(getClassForQuery());

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected BasicCrudRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    protected abstract String getSqlQueryReadById();

    protected abstract String getSqlQueryCreate();

    protected abstract String getSqlQueryUpdate();

    protected abstract String getSqlQueryDelete();

    protected abstract Class<T> getClassForQuery();

    protected abstract SqlParameterSource getSqlParameterSource(T entity);


    public Long create(T entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(getSqlQueryCreate(), getSqlParameterSource(entity), keyHolder);
        return (Long) keyHolder.getKeys().get("id");
    }

    public Optional<T> findById(Long id) {
        List<T> list = namedParameterJdbcTemplate.query(getSqlQueryReadById(), new MapSqlParameterSource("id", id), new BeanPropertyRowMapper<>(getClassForQuery()));
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    public boolean update(T entity) {
        return namedParameterJdbcTemplate.update(getSqlQueryUpdate(), getSqlParameterSource(entity)) != 0;
    }

    public boolean delete(Long id) {
        return namedParameterJdbcTemplate.update(getSqlQueryDelete(), new MapSqlParameterSource("id", id)) != 0;
    }

    public List<T> query(SqlSpecification specification) {
        return namedParameterJdbcTemplate.query(specification.toSql(), specification.params(), rowMapper);
    }
}
