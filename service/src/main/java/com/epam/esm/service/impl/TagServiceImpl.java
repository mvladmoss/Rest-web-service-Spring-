package com.epam.esm.service.impl;

import com.epam.esm.converter.DtoConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ObjectNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.specification.TagSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class TagServiceImpl implements TagService {

    private static final String TAG_NOT_FOUND_EXCEPTION = "tag.not.found";

    private final TagRepository tagRepository;
    private final DtoConverter<Tag, TagDto> dtoConverter;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, DtoConverter<Tag, TagDto> dtoConverter) {
        this.tagRepository = tagRepository;
        this.dtoConverter = dtoConverter;
    }

    @Transactional
    @Override
    public TagDto create(TagDto tagDto) {
        Tag tag = dtoConverter.unconvert(tagDto);
        Long tagId = tagRepository.create(tag);
        return findById(tagId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TagDto> read(String sortBy, String findByName, String sortOrder) {
        List<Tag> allTag = tagRepository.query(new TagSpecification(sortBy, findByName, sortOrder));
        return allTag.stream()
                .map(dtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TagDto update(TagDto entity) {
        throw new UnsupportedOperationException("Update operation is not permitted");
    }

    @Transactional
    @Override
    public void delete(Long id) {
        tagRepository.findById(id).map(tag -> {
            tagRepository.detachTagFromCertificate(id);
            tagRepository.delete(id);
            return tag;
        }).orElseThrow(() -> new ObjectNotFoundException("Tag with such id not found", TAG_NOT_FOUND_EXCEPTION, id));
    }

    @Transactional(readOnly = true)
    @Override
    public TagDto findById(Long id) {
        return tagRepository.findById(id)
                .map(dtoConverter::convert)
                .orElseThrow(() -> new ObjectNotFoundException("Tag with such id not found"
                        , TAG_NOT_FOUND_EXCEPTION, id));
    }
}
