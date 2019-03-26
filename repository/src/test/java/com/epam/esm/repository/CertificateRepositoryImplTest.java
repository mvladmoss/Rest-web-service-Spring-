package com.epam.esm.repository;

import com.epam.esm.config.TestRepositoryConfiguration;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.specification.CertificateSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ContextConfiguration(classes = TestRepositoryConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CertificateRepositoryImplTest {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final Tag FIRST_TAG = new Tag(1L, "Tag2");
    private static final Tag SECOND_TAG = new Tag(2L, "Tag3");
    private static final Tag THIRD_TAG = new Tag(3L, "Tag4");
    private static final Certificate SECOND_CERTIFICATE = new Certificate(2L
            , "Certificate2"
            , "First description", 6, BigDecimal.valueOf(45.0000d)
            , LocalDateTime.parse("2019-03-16 11:00:03", DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).
            toInstant(ZoneOffset.UTC)
            , LocalDateTime.parse("2019-03-15 11:00:03", DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).
            toInstant(ZoneOffset.UTC)
            , new HashSet<>());
    private static final Certificate THIRD_CERTIFICATE = new Certificate(3L, "Certificate3"
            , "Second description", 7, BigDecimal.valueOf(1456.4330)
            , LocalDateTime.parse("2019-03-15 11:00:03", DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).
            toInstant(ZoneOffset.UTC)
            , LocalDateTime.parse("2019-03-11 11:00:03", DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).
            toInstant(ZoneOffset.UTC)
            , new HashSet<>());
    private static final Certificate FOURTH_CERTIFICATE = new Certificate(4L, "Certificate4"
            , "Third description", 8, BigDecimal.valueOf(4574.4200)
            , LocalDateTime.parse("2019-02-09 11:00:03", DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).
            toInstant(ZoneOffset.UTC)
            , LocalDateTime.parse("2019-02-04 11:00:03", DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).
            toInstant(ZoneOffset.UTC)
            , new HashSet<>());

    @Autowired
    TagRepository tagRepositoryImpl;

    @Autowired
    CertificateRepository certificateRepositoryImpl;

    @Test
    @Transactional
    public void readCertificatesSortByNameSortOrderAsc() {
        List<Certificate> actualCertificates = Arrays.asList(SECOND_CERTIFICATE, THIRD_CERTIFICATE,
                FOURTH_CERTIFICATE);
        List<Certificate> expectedCertificates = certificateRepositoryImpl.query(
                new CertificateSpecification("name", null
                        , null, null, null, "asc"));
        Assert.assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    @Transactional
    public void shouldCreateCertificate() {
        Certificate newCertificate = new Certificate(10L, "Certificate1"
                , "first description", 1, BigDecimal.valueOf(100d)
                , Instant.now(), Instant.now(), new HashSet<>());
        newCertificate.getTags().add(SECOND_TAG);
        newCertificate.getTags().add(THIRD_TAG);
        Long id = certificateRepositoryImpl.create(newCertificate);
        Assert.assertTrue(certificateRepositoryImpl.findById(id).isPresent());
    }

    @Test
    @Transactional
    public void updateCertificate() {
        Certificate newCertificate = new Certificate(2L, "Certificate2"
                , "second description", 10, BigDecimal.valueOf(50d)
                , Instant.now(), Instant.now(), new HashSet<>());
        Boolean update = certificateRepositoryImpl.update(newCertificate);
        Assert.assertTrue(update);
    }

    @Test
    @Transactional
    public void notUpdateCertificateNotFound() {
        Certificate newCertificate = new Certificate(333L, "Certificate2"
                , "second description", 10, BigDecimal.valueOf(50d)
                , Instant.now(), Instant.now(), new HashSet<>());
        Boolean update = certificateRepositoryImpl.update(newCertificate);
        Assert.assertFalse(update);
    }

    @Test
    @Transactional
    public void findByIdCertificate() {
        Optional<Certificate> certificate = certificateRepositoryImpl.findById(3L);
        Assert.assertTrue(certificate.isPresent());
    }

    @Test
    @Transactional
    public void notFindByIdCertificate() {
        Optional<Certificate> certificate = certificateRepositoryImpl.findById(210L);
        Assert.assertFalse(certificate.isPresent());
    }

    @Transactional
    @Test
    public void deleteCertificate() {
        certificateRepositoryImpl.detachTagFromCertificateById(2L);
        Boolean deleteResult = tagRepositoryImpl.delete(2L);
        Assert.assertTrue(deleteResult);
    }

    @Test
    @Transactional
    public void notDeleteCertificate() {
        Boolean deleteResult = tagRepositoryImpl.delete(5L);
        Assert.assertFalse(deleteResult);
    }

    @Test
    @Transactional
    public void readCertificatesSearchByNameSortOrderDesc() {
        List<Certificate> actualCertificates = Collections.singletonList(SECOND_CERTIFICATE);
        List<Certificate> expectedCertificates = certificateRepositoryImpl.query(
                new CertificateSpecification("name", null
                        , "e2", null, null, "desc"));
        Assert.assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    @Transactional
    public void readCertificatesSearchDescriptionSortOrderAsc() {
        List<Certificate> actualCertificates = Arrays.asList(FOURTH_CERTIFICATE, THIRD_CERTIFICATE, SECOND_CERTIFICATE);
        List<Certificate> expectedCertificates = certificateRepositoryImpl.query(
                new CertificateSpecification("name", "description"
                        , null, null, null, "desc"));
        Assert.assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    @Transactional
    public void readCertificatesByTagName() {
        List<Certificate> actualCertificates = Collections.singletonList(SECOND_CERTIFICATE);
        List<Certificate> expectedCertificates = certificateRepositoryImpl.query(
                new CertificateSpecification("name", null
                        , null, null, "ag2", "asc"));
        Assert.assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    @Transactional
    public void readCertificatesByTagNameEmptyResult() {
        List<Certificate> actualCertificates = Collections.emptyList();
        List<Certificate> expectedCertificates = certificateRepositoryImpl.query(
                new CertificateSpecification("name", null
                        , null, null, "assag2", "asc"));
        Assert.assertEquals(expectedCertificates, actualCertificates);
    }

}
