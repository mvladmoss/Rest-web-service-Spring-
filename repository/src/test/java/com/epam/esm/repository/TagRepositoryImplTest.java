package com.epam.esm.repository;

import com.epam.esm.config.TestRepositoryConfiguration;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.specification.TagByCertificateId;
import com.epam.esm.specification.TagByNameSpecification;
import com.epam.esm.specification.TagSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = TestRepositoryConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TagRepositoryImplTest {

    private static final Tag SECOND_TAG = new Tag(2L, "Tag2");
    private static final Tag THIRD_TAG = new Tag(3L, "Tag3");
    private static final Tag FOURTH_TAG = new Tag(4L, "Tag4");

    @Autowired
    TagRepository tagRepositoryImpl;

    @Autowired
    CertificateRepository certificateRepositoryImpl;

    @Test
    @Transactional
    public void createTag() {
        Tag actualTag = new Tag(11L, "view1");
        Long id = tagRepositoryImpl.create(actualTag);
        Assert.assertTrue(tagRepositoryImpl.findById(id).isPresent());
    }

    @Test
    @Transactional
    public void updateTag() {
        Tag actualTag = new Tag(3L, "view2");
        Boolean update = tagRepositoryImpl.update(actualTag);
        Assert.assertTrue(update);
    }

    @Test
    @Transactional
    public void notUpdateTagNoSFound() {
        Tag actualTag = new Tag(112L, "view2");
        Boolean update = tagRepositoryImpl.update(actualTag);
        Assert.assertFalse(update);
    }


    @Test
    @Transactional
    public void findByIdTag() {
        Optional<Tag> tag = tagRepositoryImpl.findById(3L);
        Assert.assertTrue(tag.isPresent());
    }

    @Test
    @Transactional
    public void notFindByIdTagNoSuch() {
        Optional<Tag> tag = tagRepositoryImpl.findById(210L);
        Assert.assertFalse(tag.isPresent());
    }

    @Test
    @Transactional
    public void readTagsSortByNameSortOrderAsc() {
        List<Tag> actualTags = Arrays.asList(SECOND_TAG, THIRD_TAG, FOURTH_TAG);
        List<Tag> expectedTags = tagRepositoryImpl.query(new TagSpecification("name", null, "asc"));
        Assert.assertEquals(actualTags, expectedTags);
    }

    @Test
    @Transactional
    public void readTagsSortByIdSortOrderDesc() {
        List<Tag> actualTags = Arrays.asList(FOURTH_TAG, THIRD_TAG, SECOND_TAG);
        List<Tag> expectedTags = tagRepositoryImpl.query(new TagSpecification("id", null, "desc"));
        Assert.assertEquals(actualTags, expectedTags);
    }

    @Test
    @Transactional
    public void readTagsFindByNameSortByIdSortOrderDesc() {
        List<Tag> actualTags = Collections.singletonList(SECOND_TAG);
        List<Tag> expectedTags = tagRepositoryImpl.query(new TagSpecification("id", "g2", "desc"));
        Assert.assertEquals(actualTags, expectedTags);
    }

    @Test
    @Transactional
    public void readTagsFindByNameSortByNameSortOrderAsc() {
        List<Tag> actualTags = Arrays.asList(SECOND_TAG, THIRD_TAG, FOURTH_TAG);
        List<Tag> expectedTags = tagRepositoryImpl.query(new TagSpecification("name", "g", "asc"));
        Assert.assertEquals(actualTags, expectedTags);
    }

    @Test
    @Transactional
    public void detachTagFromCertificateByTagIdTest() {
        Optional<Certificate> certificateOptional = certificateRepositoryImpl.findById(1L);
        certificateOptional.ifPresent(certificate -> {
            Assert.assertEquals(2, certificate.getTags().size());
            tagRepositoryImpl.detachTagFromCertificate(2L);
            certificateRepositoryImpl.findById(1L).ifPresent(certificateWithoutOneTag -> {
                Assert.assertEquals(1, certificateWithoutOneTag.getTags().size());
            });
        });
    }

    @Test
    @Transactional
    public void findTagByCertificateId() {
        List<Tag> tags = tagRepositoryImpl.query(new TagByCertificateId(4L));
        Assert.assertEquals(1, tags.size());
        Assert.assertEquals(FOURTH_TAG, tags.get(0));
    }

    @Test
    @Transactional
    public void findTagByName() {
        List<Tag> tags = tagRepositoryImpl.query(new TagByNameSpecification("Tag2"));
        Assert.assertEquals(1, tags.size());
        Assert.assertEquals(SECOND_TAG, tags.get(0));
    }

    @Test
    @Transactional
    public void deleteTag() {
        tagRepositoryImpl.detachTagFromCertificate(2L);
        Boolean deleteResult = tagRepositoryImpl.delete(2L);
        Assert.assertTrue(deleteResult);
    }

    @Test
    @Transactional
    public void notDeleteTagNoSuch() {
        Boolean deleteResult = tagRepositoryImpl.delete(5L);
        Assert.assertFalse(deleteResult);
    }

}