package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.controllers.TagController;
import org.gamed.gamelistdatabaseservice.domain.Tag;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateTagRequestModel;
import org.gamed.gamelistdatabaseservice.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for TagController.
 */
public class TagControllerIntegrationTests {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createTag endpoint for a successful tag creation.
     */
    @Test
    public void testCreateTag_Success() {
        // Arrange
        CreateAndUpdateTagRequestModel request = new CreateAndUpdateTagRequestModel(
                "RPG"
        );

        Tag mockTag = new Tag("tag123", LocalDateTime.of(2023,1,1,1,0,0));

        when(tagService.createTag(
                request.getName()
        )).thenReturn(mockTag);

        // Act
        ResponseEntity<String> response = tagController.createTag(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(tagService, times(1)).createTag(
                request.getName()
        );
    }

    /**
     * Test the createTag endpoint when an exception occurs.
     */
    @Test
    public void testCreateTag_Failure() {
        // Arrange
        CreateAndUpdateTagRequestModel request = new CreateAndUpdateTagRequestModel(
                "RPG"
        );

        when(tagService.createTag(
                request.getName()
        )).thenThrow(new IllegalArgumentException("Invalid tag data."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tagController.createTag(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid tag data.", exception.getReason());
        verify(tagService, times(1)).createTag(
                request.getName()
        );
    }

    /**
     * Test the getTag endpoint for a successful retrieval of a tag.
     */
    @Test
    public void testGetTag_Success() {
        // Arrange
        String tagId = "tag123";
        Tag mockTag = new Tag(tagId, LocalDateTime.of(2023,1,1,1,0,0));

        when(tagService.getTag(tagId)).thenReturn(mockTag);

        // Act
        ResponseEntity<Tag> response = tagController.getTag(tagId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTag, response.getBody());
        verify(tagService, times(1)).getTag(tagId);
    }

    /**
     * Test the getTag endpoint when the tag is not found.
     */
    @Test
    public void testGetTag_NotFound() {
        // Arrange
        String tagId = "nonexistentTag";

        when(tagService.getTag(tagId))
                .thenThrow(new IllegalArgumentException("Tag not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tagController.getTag(tagId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Tag not found.", exception.getReason());
        verify(tagService, times(1)).getTag(tagId);
    }

    /**
     * Test the getAllTags endpoint for a successful retrieval of all tags.
     */
    @Test
    public void testGetAllTags_Success() {
        // Arrange
        Tag tag1 = new Tag("tag123", LocalDateTime.of(2023,1,1,1,0,0));
        Tag tag2 = new Tag("tag456", LocalDateTime.of(2023,1,1,1,0,0));
        List<Tag> mockTags = Arrays.asList(tag1, tag2);

        when(tagService.getAllTagsSortedByTimeCreated()).thenReturn(mockTags);

        // Act
        ResponseEntity<List<Tag>> response = tagController.getAllTags();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTags, response.getBody());
        verify(tagService, times(1)).getAllTagsSortedByTimeCreated();
    }

    /**
     * Test the getAllTags endpoint when an exception occurs.
     */
    @Test
    public void testGetAllTags_Failure() {
        // Arrange
        when(tagService.getAllTagsSortedByTimeCreated())
                .thenThrow(new IllegalArgumentException("Unable to retrieve tags."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tagController.getAllTags();
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Unable to retrieve tags.", exception.getReason());
        verify(tagService, times(1)).getAllTagsSortedByTimeCreated();
    }

    /**
     * Test the deleteTag endpoint for a successful deletion of a tag.
     */
    @Test
    public void testDeleteTag_Success() {
        // Arrange
        String tagId = "tag123";

        doNothing().when(tagService).deleteTag(tagId);

        // Act
        ResponseEntity<String> response = tagController.deleteTag(tagId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(tagService, times(1)).deleteTag(tagId);
    }

    /**
     * Test the deleteTag endpoint when the tag does not exist.
     */
    @Test
    public void testDeleteTag_NotFound() {
        // Arrange
        String tagId = "nonexistentTag";

        doThrow(new IllegalArgumentException("Tag not found."))
                .when(tagService).deleteTag(tagId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tagController.deleteTag(tagId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Tag not found.", exception.getReason());
        verify(tagService, times(1)).deleteTag(tagId);
    }
}
