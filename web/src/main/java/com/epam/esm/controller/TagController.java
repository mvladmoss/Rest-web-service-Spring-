package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private static final String NOT_VALID_TAG_EXCEPTION = "tag.not.valid";

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagServiceImpl) {
        this.tagService = tagServiceImpl;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTag(@PathVariable("id") Long id) {
        TagDto tagDto = tagService.findById(id);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTag(@PathVariable Long id) {
        TagDto tagDto = new TagDto();
        tagDto.setId(id);
        tagService.delete(tagDto.getId());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getTags(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "findByName", required = false) String findByName,
            @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        List<TagDto> tagDTOList = tagService.read(sortBy, findByName, sortOrder);
        return new ResponseEntity<>(tagDTOList, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> createTag(@RequestBody @Valid TagDto tagDto, BindingResult result) {
        tagDto.setId(null);
        tagDto = tagService.create(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.CREATED);
    }

}
