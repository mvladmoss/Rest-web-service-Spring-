package com.epam.esm.validation.tag;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.specification.TagByNameSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueTagNameValidator implements ConstraintValidator<UniqueTagName, String> {

    private final TagRepository tagRepository;

    @Autowired
    public UniqueTagNameValidator(TagRepository tagRepositoryImpl) {
        this.tagRepository = tagRepositoryImpl;
    }

    @Override
    public void initialize(UniqueTagName uniqueTagName) {

    }

    @Override
    public boolean isValid(String tagDtoName, ConstraintValidatorContext constraintValidatorContext) {
        List<Tag> allTag = tagRepository.query(new TagByNameSpecification(tagDtoName));
        return allTag.size() != 1;
    }

}
