package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/certificates", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    private static final String NOT_VALID_CERTIFICATE_EXCEPTION = "certificate.not.valid";

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateServiceImpl) {
        this.certificateService = certificateServiceImpl;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CertificateDto> getGiftCertificate(@PathVariable("id") Long id) {
        CertificateDto certificateDto = certificateService.findById(id);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CertificateDto>> getGiftCertificate(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "certName", required = false) String certName,
            @RequestParam(value = "certDescription", required = false) String certDescription,
            @RequestParam(value = "sortOrder", required = false) String sortOrder,
            @RequestParam(value = "tagId", required = false) String tagId,
            @RequestParam(value = "tagName", required = false) String tagName) {
        List<CertificateDto> certificateDTOList = certificateService.read(sortBy, certDescription, certName, tagId, tagName, sortOrder);
        return new ResponseEntity<>(certificateDTOList, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteGiftCertificate(@PathVariable Long id) {
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(id);
        certificateService.delete(certificateDto.getId());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDto> createGiftCertificate(@Valid @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(null);
        certificateDto = certificateService.create(certificateDto);
        return new ResponseEntity<>(certificateDto, HttpStatus.CREATED);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDto> updateGiftCertificate(@PathVariable Long id, @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        certificateDto = certificateService.update(certificateDto);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }


}
