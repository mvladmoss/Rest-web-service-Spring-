package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService extends CrudService<TagDto> {
    List<TagDto> read(String sortBy, String findByName, String sortOrder);
}
