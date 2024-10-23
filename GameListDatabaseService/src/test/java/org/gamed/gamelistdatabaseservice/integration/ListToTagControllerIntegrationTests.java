package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.controllers.ListToTagController;
import org.gamed.gamelistdatabaseservice.domain.GameList;
import org.gamed.gamelistdatabaseservice.domain.ListToTag;
import org.gamed.gamelistdatabaseservice.domain.Tag;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateListToTagRequestModel;
import org.gamed.gamelistdatabaseservice.service.ListToTagService;
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
 * Test class for ListToTagController.
 */
public class ListToTagControllerIntegrationTests {

    @Mock
    private ListToTagService listToTagService;

    @InjectMocks
    private ListToTagController listToTagController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createListToTag endpoint for a successful ListToTag creation.
     */
    @Test
    public void testCreateListToTag_Success() {
        // Arrange
        CreateAndUpdateListToTagRequestModel request = new CreateAndUpdateListToTagRequestModel(
                "list123",
                "tag456"
        );

        GameList mockList = new GameList("user789", "Favorites", "My favorite games.");
        Tag mockTag = new Tag("tag456", LocalDateTime.of(2023,1,1,1,0,0));

        ListToTag mockListToTag = new ListToTag(mockList.getId(), mockTag.getId());

        when(listToTagService.createListToTag(
                request.getListId(),
                request.getTagId()
        )).thenReturn(mockListToTag);

        // Act
        ResponseEntity<String> response = listToTagController.createListToTag(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(listToTagService, times(1)).createListToTag(
                request.getListId(),
                request.getTagId()
        );
    }

    /**
     * Test the createListToTag endpoint when an exception occurs.
     */
    @Test
    public void testCreateListToTag_Failure() {
        // Arrange
        CreateAndUpdateListToTagRequestModel request = new CreateAndUpdateListToTagRequestModel(
                "list123",
                "tag456"
        );

        when(listToTagService.createListToTag(
                request.getListId(),
                request.getTagId()
        )).thenThrow(new IllegalArgumentException("Invalid List or Tag ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            listToTagController.createListToTag(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid List or Tag ID.", exception.getReason());
        verify(listToTagService, times(1)).createListToTag(
                request.getListId(),
                request.getTagId()
        );
    }

    /**
     * Test the getListToTag endpoint for a successful retrieval of a ListToTag.
     */
    @Test
    public void testGetListToTag_Success() {
        // Arrange
        String listToTagId = "listToTag789";
        GameList mockList = new GameList("user789", "Favorites", "My favorite games.");
        Tag mockTag = new Tag("tag456", LocalDateTime.of(2023,1,1,1,0,0));
        ListToTag mockListToTag = new ListToTag(mockList.getId(), mockTag.getId());

        when(listToTagService.getListToTagById(listToTagId)).thenReturn(mockListToTag);

        // Act
        ResponseEntity<ListToTag> response = listToTagController.getListToTag(listToTagId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockListToTag, response.getBody());
        verify(listToTagService, times(1)).getListToTagById(listToTagId);
    }

    /**
     * Test the getListToTag endpoint when the ListToTag is not found.
     */
    @Test
    public void testGetListToTag_NotFound() {
        // Arrange
        String listToTagId = "nonexistentListToTag";

        when(listToTagService.getListToTagById(listToTagId))
                .thenThrow(new IllegalArgumentException("ListToTag not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            listToTagController.getListToTag(listToTagId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("ListToTag not found.", exception.getReason());
        verify(listToTagService, times(1)).getListToTagById(listToTagId);
    }

    /**
     * Test the getAllListToGames endpoint for a successful retrieval of all ListToTags.
     */
    @Test
    public void testGetAllListToGames_Success() {
        // Arrange
        GameList list1 = new GameList("user789", "Favorites", "My favorite games.");
        Tag tag1 = new Tag("tag456", LocalDateTime.of(2023,1,1,1,0,0));
        ListToTag listToTag1 = new ListToTag(list1.getId(), tag1.getId());

        GameList list2 = new GameList("user790", "Wishlist", "Games I want to play.");
        Tag tag2 = new Tag("tag457", LocalDateTime.of(2023,1,1,1,0,0));
        ListToTag listToTag2 = new ListToTag(list2.getId(), tag2.getId());

        List<ListToTag> mockListToTags = Arrays.asList(listToTag1, listToTag2);

        when(listToTagService.getAllListToTags()).thenReturn(mockListToTags);

        // Act
        ResponseEntity<List<ListToTag>> response = listToTagController.getAllListToTags();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockListToTags, response.getBody());
        verify(listToTagService, times(1)).getAllListToTags();
    }

    /**
     * Test the getAllListToGames endpoint when an exception occurs.
     */
    @Test
    public void testGetAllListToGames_Failure() {
        // Arrange
        when(listToTagService.getAllListToTags())
                .thenThrow(new IllegalArgumentException("Unable to retrieve ListToTags."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            listToTagController.getAllListToTags();
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Unable to retrieve ListToTags.", exception.getReason());
        verify(listToTagService, times(1)).getAllListToTags();
    }

    /**
     * Test the deleteListToGame endpoint for a successful deletion of a ListToTag.
     */
    @Test
    public void testDeleteListToGame_Success() {
        // Arrange
        String listToTagId = "listToTag789";

        doNothing().when(listToTagService).deleteListToTagById(listToTagId);

        // Act
        ResponseEntity response = listToTagController.deleteListToTag(listToTagId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listToTagService, times(1)).deleteListToTagById(listToTagId);
    }

    /**
     * Test the deleteListToGame endpoint when the ListToTag does not exist.
     */
    @Test
    public void testDeleteListToGame_NotFound() {
        // Arrange
        String listToTagId = "nonexistentListToTag";

        doThrow(new IllegalArgumentException("ListToTag not found."))
                .when(listToTagService).deleteListToTagById(listToTagId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            listToTagController.deleteListToTag(listToTagId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("ListToTag not found.", exception.getReason());
        verify(listToTagService, times(1)).deleteListToTagById(listToTagId);
    }
}
