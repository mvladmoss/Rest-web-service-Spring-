package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;

public interface CertificateRepository extends CrudRepository<Certificate> {
    void detachTagFromCertificateById(Long certificateId);

    void attachTagToCertificate(Long certificateId, Long tagId);
}
