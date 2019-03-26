package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;

import java.util.List;

public interface CertificateService extends CrudService<CertificateDto> {

    List<CertificateDto> read(String sortBy, String searchByDescription, String searchByName, String tagId, String tagName, String orderSort);

}