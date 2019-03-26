package com.epam.esm.service;

import com.epam.esm.converter.CertificateDtoConverter;
import com.epam.esm.converter.DtoConverter;
import com.epam.esm.converter.TagDtoConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ObjectNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.specification.SqlSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class CertificateServiceImplTest {

    private static final Tag FIRST_TAG = new Tag(1L, "Tag2");
    private static final Tag SECOND_TAG = new Tag(2L, "Tag3");
    private static final Tag THIRD_TAG = new Tag(3L, "Tag4");
    private static final TagDto FIRST_TAG_DTO = new TagDto(1L, "Tag2");
    private static final TagDto SECOND_TAG_DTO = new TagDto(2L, "Tag3");
    private static final TagDto THIRD_TAG_DTO = new TagDto(3L, "Tag4");
    private static final Instant TIME_NOW = Instant.now();

    private static final Certificate FIRST_CERTIFICATE = new Certificate(1L, "Certificate1"
            , "first description", 5, BigDecimal.valueOf(100d), TIME_NOW
            , TIME_NOW, new HashSet<>(Arrays.asList(FIRST_TAG,THIRD_TAG)));
    private static final Certificate SECOND_CERTIFICATE = new Certificate(2L, "Certificate2"
            , "second description", 2, BigDecimal.valueOf(400d)
            , TIME_NOW, TIME_NOW, new HashSet<>(Arrays.asList(SECOND_TAG,THIRD_TAG)));
    private static final Certificate THIRD_CERTIFICATE = new Certificate(3L, "Certificate3"
            , "third description", 12, BigDecimal.valueOf(600d)
            , TIME_NOW, TIME_NOW, new HashSet<>(Arrays.asList(FIRST_TAG,SECOND_TAG)));
    private static final CertificateDto FIRST_CERTIFICATE_DTO = new CertificateDto(1L, "Certificate1"
            , "first description", 5,
            BigDecimal.valueOf(100d), TIME_NOW, TIME_NOW, new HashSet<>(Arrays.asList(FIRST_TAG_DTO,THIRD_TAG_DTO)));
    private static final CertificateDto SECOND_CERTIFICATE_DTO = new CertificateDto(2L, "Certificate2"
            , "second description", 2, BigDecimal.valueOf(400d)
            , Instant.now(), Instant.now(), new HashSet<>(Arrays.asList(SECOND_TAG_DTO,THIRD_TAG_DTO)));
    private static final CertificateDto THIRD_CERTIFICATE_DTO = new CertificateDto(3L, "Certificate3"
            , "third description", 12, BigDecimal.valueOf(600d)
            , TIME_NOW, TIME_NOW, new HashSet<>(Arrays.asList(FIRST_TAG_DTO,SECOND_TAG_DTO)));


    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagService tagService;

    private DtoConverter<Tag,TagDto> dtoTagConverter = new TagDtoConverter();

    private DtoConverter<Certificate,CertificateDto> dtoCertificateConverter = new CertificateDtoConverter(dtoTagConverter);

    @InjectMocks
    private CertificateServiceImpl certificateService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        certificateService = new CertificateServiceImpl(certificateRepository, tagService
                , dtoCertificateConverter, tagRepository, dtoTagConverter);
    }

    @Test
    public void shouldFindById() {
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(FIRST_CERTIFICATE));
        Mockito.when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(FIRST_TAG));
        Mockito.when(tagRepository.query(Mockito.any(SqlSpecification.class))).thenReturn(Arrays.asList(FIRST_TAG, THIRD_TAG));
        CertificateDto certificateDto = certificateService.findById(1L);
        Assert.assertEquals(FIRST_CERTIFICATE_DTO, certificateDto);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldNotGetById() {
        Mockito.when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(FIRST_TAG));
        certificateService.findById(2L);
    }


    @Test
    public void shouldGetCertificateSortByIdOrderAsc() {
        Mockito.when(certificateRepository.query(Mockito.any(SqlSpecification.class))).thenReturn(Arrays.asList(
                FIRST_CERTIFICATE,SECOND_CERTIFICATE,THIRD_CERTIFICATE));
        Mockito.when(tagRepository.query(Mockito.any(SqlSpecification.class))).thenReturn(Arrays.asList(FIRST_TAG, THIRD_TAG));
        Mockito.when(tagRepository.query(Mockito.any(SqlSpecification.class))).thenReturn(Arrays.asList(FIRST_TAG, THIRD_TAG));
        List<CertificateDto> tags = certificateService.read("id", null, null, null
                , null, "asc");
        Assert.assertEquals(FIRST_CERTIFICATE_DTO, tags.get(0));
    }

    @Test
    public void shouldNotGetTagByName() {
        Mockito.when(certificateRepository.query(Mockito.any(SqlSpecification.class))).thenReturn(Collections.emptyList());
        List<CertificateDto> readTags = certificateService.read(null, null, null, null, null, null);
        Assert.assertEquals(0, readTags.size());
    }

    @Test
    public void shouldCreateTag() {
        Mockito.when(certificateRepository.create(Mockito.any(Certificate.class))).thenReturn(FIRST_CERTIFICATE.getId());
        Mockito.when(certificateRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(FIRST_CERTIFICATE));
        Mockito.when(tagRepository.findById(Mockito.eq(FIRST_TAG.getId()))).thenReturn(Optional.of(FIRST_TAG));
        Mockito.when(tagRepository.findById(Mockito.eq(THIRD_TAG.getId()))).thenReturn(Optional.of(THIRD_TAG));
        Mockito.when(tagRepository.query(Mockito.any(SqlSpecification.class))).thenReturn(Arrays.asList(FIRST_TAG, THIRD_TAG));
        CertificateDto certificateDto = certificateService.create(FIRST_CERTIFICATE_DTO);
        Assert.assertEquals(FIRST_CERTIFICATE_DTO, certificateDto);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldNotCreateTag() {
        Mockito.when(certificateRepository.create(Mockito.any(Certificate.class))).thenReturn(FIRST_CERTIFICATE.getId());
        Mockito.when(tagRepository.findById(Mockito.eq(FIRST_TAG.getId()))).thenReturn(Optional.empty());
        CertificateDto certificateDto = certificateService.create(FIRST_CERTIFICATE_DTO);
        Assert.assertEquals(FIRST_CERTIFICATE_DTO, certificateDto);
    }

    @Test
    public void shouldDeleteCertificateById() {
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(FIRST_CERTIFICATE));
        Mockito.when(certificateRepository.delete(Mockito.anyLong())).thenReturn(true);
        certificateService.delete(1L);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldNotDeleteCertificateByIdCauseNotFound() {
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        certificateService.delete(6L);
    }
}
