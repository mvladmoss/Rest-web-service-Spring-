package com.epam.esm.service;

import com.epam.esm.converter.DtoConverter;
import com.epam.esm.converter.TagDtoConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ObjectNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.specification.SqlSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class TagServiceImplTest {
    private static final Tag SECOND_TAG = new Tag(2L, "Tag2");
    private static final Tag THIRD_TAG = new Tag(3L, "Tag3");
    private static final Tag FOURTH_TAG = new Tag(4L, "Tag4");
    private static final TagDto SECOND_TAG_DTO = new TagDto(2L, "Tag2");
    private static final TagDto THIRD_TAG_DTO = new TagDto(3L, "Tag3");
    private static final TagDto FOURTH_TAG_DTO = new TagDto(4L, "Tag4");

    @InjectMocks
    private TagServiceImpl service;

    @Mock
    private TagRepository repository;

    @Mock
    private DtoConverter<Tag,TagDto> converter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetById() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(SECOND_TAG));
        Mockito.when(converter.convert(Mockito.any(Tag.class))).thenReturn(SECOND_TAG_DTO);

        TagDto tagDto = service.findById(2L);

        Assert.assertEquals(SECOND_TAG_DTO, tagDto);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldNotGetById() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        service.findById(2L);
    }

    @Test
    public void shouldGetTagByName() {
        List<Tag> dsa = new ArrayList<>();
        dsa.add(SECOND_TAG);
        Mockito.when(repository.query(Mockito.any(SqlSpecification.class))).thenReturn(dsa);
        Mockito.when(converter.convert(Mockito.any(Tag.class))).thenReturn(SECOND_TAG_DTO);

        List<TagDto> tags = service.read(null, "Tag2", null);

        Assert.assertEquals(SECOND_TAG_DTO, tags.get(0));
    }

    @Test
    public void shouldNotGetTagByName() {
        Mockito.when(repository.query(Mockito.any(SqlSpecification.class))).thenReturn(Collections.emptyList());
        List<TagDto> readTags = service.read(null, null, null);
        Assert.assertEquals(readTags.size(), 0);
    }

    @Test
    public void shouldGetTagsAndSortByName() {
        Mockito.when(repository.query(Mockito.any(SqlSpecification.class))).thenReturn(
                Arrays.asList(SECOND_TAG, THIRD_TAG, FOURTH_TAG));
        Mockito.when(converter.convert(Mockito.eq(SECOND_TAG))).thenReturn(SECOND_TAG_DTO);
        Mockito.when(converter.convert(Mockito.eq(THIRD_TAG))).thenReturn(THIRD_TAG_DTO);
        Mockito.when(converter.convert(Mockito.eq(FOURTH_TAG))).thenReturn(FOURTH_TAG_DTO);
        List<TagDto> expectedTags = service.read("name", "Tag2", null);
        List<TagDto> actualTags = Arrays.asList(SECOND_TAG_DTO, THIRD_TAG_DTO, FOURTH_TAG_DTO);
        Assert.assertEquals(actualTags, expectedTags);
    }

    @Test
    public void shouldCreateTag() {
        Mockito.when(repository.create(Mockito.any(Tag.class))).thenReturn(SECOND_TAG.getId());
        Mockito.when(converter.convert(Mockito.any(Tag.class))).thenReturn(SECOND_TAG_DTO);
        Mockito.when(converter.unconvert(Mockito.any(TagDto.class))).thenReturn(SECOND_TAG);
        Mockito.when(repository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(SECOND_TAG));

        TagDto tagDto = service.create(SECOND_TAG_DTO);

        Assert.assertEquals(SECOND_TAG_DTO, tagDto);
    }

    @Test
    public void shouldDeleteTagById() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(SECOND_TAG));
        Mockito.when(repository.delete(Mockito.anyLong())).thenReturn(true);
        service.delete(2L);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldNotDeleteTagByIdCauseNotFound() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        service.delete(6L);
    }

}
