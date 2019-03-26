package com.epam.esm.validation.certificate;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.specification.CertificateByNameSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueCertificateNameValidator implements ConstraintValidator<UniqueCertificateName, String> {

    private final CertificateRepository certificateRepository;

    @Autowired
    public UniqueCertificateNameValidator(CertificateRepository certificateRepositoryImpl) {
        this.certificateRepository = certificateRepositoryImpl;
    }

    @Override
    public void initialize(UniqueCertificateName uniqueCertificateName) {
    }

    @Override
    public boolean isValid(String certificateName, ConstraintValidatorContext constraintValidatorContext) {
        return certificateRepository.query(new CertificateByNameSpecification(certificateName))
                .size() == 0;
    }
}
