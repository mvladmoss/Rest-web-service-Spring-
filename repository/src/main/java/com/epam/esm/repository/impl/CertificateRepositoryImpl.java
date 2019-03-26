package com.epam.esm.repository.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class CertificateRepositoryImpl extends BasicCrudRepository<Certificate> implements CertificateRepository {

    private static final String CREATE_SQL_CERTIFICATE =
            "insert into gift_certificate (name, description, price, date_of_creation, date_of_modification, duration_in_days) " +
                    "values (:name, :description, :price, :dateOfCreation, :dateOfModification, :durationInDays);";
    private static final String UPDATE_SQL_CERTIFICATE =
            "update gift_certificate " +
                    "set name = :name, description = :description, price = :price," +
                    " date_of_modification = :dateOfModification, duration_in_days = :durationInDays " +
                    "where id = :id;";
    private static final String READ_SQL_CERTIFICATE =
            "select id, name, description, price, date_of_creation, date_of_modification, duration_in_days " +
                    "from gift_certificate where id = :id";
    private static final String DELETE_SQL_CERTIFICATE =
            "delete from gift_certificate where id = :id";
    private static final String ATTACH_TAG_FOR_CERTIFICATE_SQL_BY_CERTICIATE_ID = "insert into tag_gift_certificate(gift_certificate_id, tag_id) " +
            "values(:certificateId, :tagId);";
    private static final String DETACH_TAG_FROM_CERTIFICATE_SQL_BY_CERTIFICATE_ID =
            "delete from tag_gift_certificate where gift_certificate_id = :certificateId";

    @Autowired
    public CertificateRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public void attachTagToCertificate(Long certificateId, Long tagId) {
        namedParameterJdbcTemplate.update(ATTACH_TAG_FOR_CERTIFICATE_SQL_BY_CERTICIATE_ID, new MapSqlParameterSource("certificateId", certificateId).addValue("tagId", tagId));
    }

    @Override
    public void detachTagFromCertificateById(Long certificateId) {
        namedParameterJdbcTemplate.update(DETACH_TAG_FROM_CERTIFICATE_SQL_BY_CERTIFICATE_ID, new MapSqlParameterSource("certificateId", certificateId));
    }

    @Override
    protected String getSqlQueryReadById() {
        return READ_SQL_CERTIFICATE;
    }

    @Override
    protected String getSqlQueryCreate() {
        return CREATE_SQL_CERTIFICATE;
    }

    @Override
    protected String getSqlQueryUpdate() {
        return UPDATE_SQL_CERTIFICATE;
    }

    @Override
    protected String getSqlQueryDelete() {
        return DELETE_SQL_CERTIFICATE;
    }

    @Override
    protected Class<Certificate> getClassForQuery() {
        return Certificate.class;
    }

    @Override
    public SqlParameterSource getSqlParameterSource(Certificate certificate) {
        return new MapSqlParameterSource()
                .addValue("name", certificate.getName())
                .addValue("description", certificate.getDescription())
                .addValue("price", certificate.getPrice())
                .addValue("dateOfCreation", Timestamp.from(certificate.getDateOfCreation()))
                .addValue("dateOfModification", Timestamp.from(certificate.getDateOfModification()))
                .addValue("durationInDays", certificate.getDurationInDays())
                .addValue("id", certificate.getId());

    }
}
