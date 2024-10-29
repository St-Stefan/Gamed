package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.domain.Tag;
import org.gamed.gamelistdatabaseservice.repository.TagRepository;
import org.gamed.gamelistdatabaseservice.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceIntegrationTests {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Test
    void testCreateTagSuccess() {
        String name = "TestTag";

        when(tagRepository.existsByName(name)).thenReturn(false);
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tag result = tagService.createTag(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNotNull(result.getTimeCreated());

        verify(tagRepository).existsByName(name);
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void testCreateTagThrowsExceptionWhenNameIsBlank() {
        String name = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tagService.createTag(name)
        );

        assertEquals("Tag name cannot be blank.", exception.getMessage());

        verify(tagRepository, never()).existsByName(anyString());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void testCreateTagThrowsExceptionWhenTagAlreadyExists() {
        String name = "TestTag";

        when(tagRepository.existsByName(name)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tagService.createTag(name)
        );

        assertEquals("A tag with this name already exists.", exception.getMessage());

        verify(tagRepository).existsByName(name);
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void testGetTagThrowsExceptionWhenNotFound() {
        String id = "tagId123";

        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tagService.getTag(id)
        );

        assertEquals("Given Tag id does not correspond to any Tag. ID: " + id, exception.getMessage());

        verify(tagRepository).findById(id);
    }

    @Test
    void testDeleteTagSuccess() {
        String id = "tagId123";

        when(tagRepository.existsById(id)).thenReturn(true);

        tagService.deleteTag(id);

        verify(tagRepository).existsById(id);
        verify(tagRepository).deleteById(id);
    }

    @Test
    void testDeleteTagThrowsExceptionWhenNotFound() {
        String id = "tagId123";

        when(tagRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tagService.deleteTag(id)
        );

        assertEquals("Given Tag id does not correspond to any Tag. ID: " + id, exception.getMessage());

        verify(tagRepository).existsById(id);
        verify(tagRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetTagsByTimeRange() {
        int startTime = 1;
        int endTime = 10;
        List<Tag> tags = Arrays.asList(
                new Tag("Tag1", LocalDateTime.now()),
                new Tag("Tag2", LocalDateTime.now())
        );

        when(tagRepository.getTagsByTimeRange(startTime, endTime)).thenReturn(tags);

        List<Tag> result = tagService.getTagsByTimeRange(startTime, endTime);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(tags, result);

        verify(tagRepository).getTagsByTimeRange(startTime, endTime);
    }

    @Test
    void testGetAllTagsSortedByTimeCreated() {
        List<Tag> tags = Arrays.asList(
                new Tag("Tag1", LocalDateTime.now()),
                new Tag("Tag2", LocalDateTime.now())
        );

        when(tagRepository.getAllTagsSortedByTimeCreated()).thenReturn(tags);

        List<Tag> result = tagService.getAllTagsSortedByTimeCreated();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(tags, result);

        verify(tagRepository).getAllTagsSortedByTimeCreated();
    }

    @Test
    void testGetTagByNameSuccess() {
        String name = "TestTag";
        Tag tag = new Tag(name, LocalDateTime.now());

        when(tagRepository.getTagByName(name)).thenReturn(tag);

        Tag result = tagService.getTagByName(name);

        assertNotNull(result);
        assertEquals(tag, result);

        verify(tagRepository).getTagByName(name);
    }

    @Test
    void testGetTagByNameThrowsExceptionWhenNotFound() {
        String name = "NonExistentTag";

        when(tagRepository.getTagByName(name)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tagService.getTagByName(name)
        );

        assertEquals("No tag found with the name: " + name, exception.getMessage());

        verify(tagRepository).getTagByName(name);
    }
}
