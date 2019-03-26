package com.epam.esm.service.impl;

import com.epam.esm.converter.DtoConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NameAlreadyExistException;
import com.epam.esm.exception.ObjectNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.specification.CertificateByNameSpecification;
import com.epam.esm.specification.CertificateSpecification;
import com.epam.esm.specification.TagByCertificateId;
import com.epam.esm.specification.TagByNameSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class CertificateServiceImpl implements CertificateService {

    private static final String CERTIFICATE_NOT_FOUND_EXCEPTION = "certificate.not.found";
    private static final String TAG_NOT_FOUND_EXCEPTION = "tag.not.found";
    private static final String CERTIFICATE_ALREADY_EXIST_EXCEPTION = "certificate.already_exist";
    private static final String CERTIFICATE_NOT_FOUND_CERTIFICATE = "Certificate with such id not found";
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final DtoConverter<Certificate, CertificateDto> dtoCertificateConverter;
    private final DtoConverter<Tag, TagDto> dtoTagConverter;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepositoryImpl, TagService tagServiceImpl,
                                  DtoConverter<Certificate, CertificateDto> certificateDtoConverter,
                                  TagRepository tagRepositoryImpl, DtoConverter<Tag, TagDto> dtoTagConverter) {
        this.certificateRepository = certificateRepositoryImpl;
        this.tagService = tagServiceImpl;
        this.dtoCertificateConverter = certificateDtoConverter;
        this.dtoTagConverter = dtoTagConverter;
        this.tagRepository = tagRepositoryImpl;
    }

    @Transactional
    @Override
    public CertificateDto create(CertificateDto certificateDto) {
        certificateDto.setDateOfCreation(Instant.now());
        certificateDto.setDateOfModification(Instant.now());
        Long certificateId = certificateRepository.create(dtoCertificateConverter.unconvert(certificateDto));
        attachTagsToCertificate(certificateId, certificateDto.getTags());
        return findById(certificateId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CertificateDto> read(String sortBy, String searchByDescription, String searchByName, String tagId
            , String tagName, String orderSort) {
        List<Certificate> allCertificates = certificateRepository.query(new CertificateSpecification(sortBy
                , searchByDescription, searchByName, tagId, tagName, orderSort));
        if (allCertificates.isEmpty()) {
            return Collections.emptyList();
        }
        return allCertificates
                .stream()
                .map(certificate -> {
                    CertificateDto certificateDto = dtoCertificateConverter.convert(certificate);
                    List<Tag> tags = tagRepository.query(new TagByCertificateId(certificateDto.getId()));
                    Set<TagDto> tagSet = tags.stream()
                            .map(dtoTagConverter::convert)
                            .collect(Collectors.toSet());
                    certificateDto.setTags(tagSet);
                    return certificateDto;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public CertificateDto update(CertificateDto certificateDto) {
        List<Certificate> certificates = certificateRepository.query(new CertificateByNameSpecification(
                certificateDto.getName()));
        if (certificates.size() == 1) {
            Certificate certificate = certificates.get(0);
            if (certificate.getName().equals(certificateDto.getName()) && !certificate.getId().equals(certificateDto.getId())) {
                throw new NameAlreadyExistException(CERTIFICATE_NOT_FOUND_CERTIFICATE
                        , CERTIFICATE_ALREADY_EXIST_EXCEPTION, certificate.getName());
            }

        }
        certificateRepository.detachTagFromCertificateById(certificateDto.getId());
        certificateDto.setDateOfModification(Instant.now());
        certificateRepository.update(dtoCertificateConverter.unconvert(certificateDto));
        attachTagsToCertificate(certificateDto.getId(), certificateDto.getTags());
        return findById(certificateDto.getId());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        certificateRepository.findById(id).map(certificate -> {
            certificateRepository.detachTagFromCertificateById(id);
            certificateRepository.delete(id);
            return certificate;
        }).orElseThrow(() -> new ObjectNotFoundException(CERTIFICATE_NOT_FOUND_CERTIFICATE, CERTIFICATE_NOT_FOUND_EXCEPTION, id));

    }

    @Transactional(readOnly = true)
    @Override
    public CertificateDto findById(Long id) {
        return certificateRepository.findById(id)
                .map(dtoCertificateConverter::convert)
                .map(certificateDto -> {
                    List<Tag> tags = tagRepository.query(new TagByCertificateId(id));
                    certificateDto.setTags(tags.stream()
                            .map(dtoTagConverter::convert)
                            .collect(Collectors.toSet()));
                    return certificateDto;
                }).orElseThrow(() -> new ObjectNotFoundException(CERTIFICATE_NOT_FOUND_CERTIFICATE
                        , CERTIFICATE_NOT_FOUND_EXCEPTION, id));
    }

    private void attachTagsToCertificate(Long certificateId, @NotNull Set<TagDto> tags) {
        for (TagDto tag : tags) {
            Long tagId = tag.getId();
            if (tagId != null && !tagRepository.findById(tagId).isPresent()) {
                throw new ObjectNotFoundException(CERTIFICATE_NOT_FOUND_CERTIFICATE
                        , TAG_NOT_FOUND_EXCEPTION, tagId);
            } else {
                List<TagDto> existTags = tagRepository.query(new TagByNameSpecification(tag.getName()))
                        .stream()
                        .map(dtoTagConverter::convert)
                        .collect(Collectors.toList());

                if (CollectionUtils.isEmpty(existTags)) {
                    TagDto tagDTO = tagService.create(tag);
                    tagId = tagDTO.getId();
                } else {
                    tagId = existTags.get(0).getId();
                }
            }
            certificateRepository.attachTagToCertificate(certificateId, tagId);
        }

    }
}
