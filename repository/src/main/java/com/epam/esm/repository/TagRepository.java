package com.epam.esm.repository;


import com.epam.esm.entity.Tag;

public interface TagRepository extends CrudRepository<Tag> {
    void detachTagFromCertificate(Long tagId);
}
