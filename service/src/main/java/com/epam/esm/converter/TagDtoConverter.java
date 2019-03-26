package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;


@Component
public class TagDtoConverter implements DtoConverter<Tag, TagDto> {
    @Override
    public TagDto convert(Tag tag) {
        TagDto tagDto = new TagDto();
        if (tag != null) {
            tagDto.setId(tag.getId());
            tagDto.setName(tag.getName());
        }
        return tagDto;
    }

    @Override
    public Tag unconvert(TagDto tagDto) {
        Tag tag = new Tag();
        if (tagDto != null) {
            tag.setId(tagDto.getId());
            tag.setName(tagDto.getName());
        }
        return tag;
    }

}
