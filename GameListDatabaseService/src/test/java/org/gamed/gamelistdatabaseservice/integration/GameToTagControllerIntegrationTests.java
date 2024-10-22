package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.controllers.GameToTagController;
import org.gamed.gamelistdatabaseservice.domain.Game;
import org.gamed.gamelistdatabaseservice.domain.GameToTag;
import org.gamed.gamelistdatabaseservice.domain.Tag;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateGameToTagRequestModel;
import org.gamed.gamelistdatabaseservice.service.GameToTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for GameToTagController.
 */
public class GameToTagControllerIntegrationTests {

    @Mock
    private GameToTagService gameToTagService;

    @InjectMocks
    private GameToTagController gameToTagController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createGameToTag endpoint for a successful GameToTag creation.
     */
    @Test
    public void testCreateGameToTag_Success() {
        // Arrange
        CreateAndUpdateGameToTagRequestModel request = new CreateAndUpdateGameToTagRequestModel(
                "game123",
                "tag456"
        );

        GameToTag mockGameToTag = new GameToTag(new Game().getId(), new Tag().getId());

        when(gameToTagService.createGameToTag(
                request.getGameId(),
                request.getTagId()
        )).thenReturn(mockGameToTag);

        // Act
        ResponseEntity<String> response = gameToTagController.createGameToTag(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(gameToTagService, times(1)).createGameToTag(
                request.getGameId(),
                request.getTagId()
        );
    }

    /**
     * Test the createGameToTag endpoint when an exception occurs.
     */
    @Test
    public void testCreateGameToTag_Failure() {
        // Arrange
        CreateAndUpdateGameToTagRequestModel request = new CreateAndUpdateGameToTagRequestModel(
                "game123",
                "tag456"
        );

        when(gameToTagService.createGameToTag(
                request.getGameId(),
                request.getTagId()
        )).thenThrow(new IllegalArgumentException("Invalid Game or Tag ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameToTagController.createGameToTag(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid Game or Tag ID.", exception.getReason());
        verify(gameToTagService, times(1)).createGameToTag(
                request.getGameId(),
                request.getTagId()
        );
    }

    /**
     * Test the getGameToTag endpoint for a successful retrieval of a GameToTag.
     */
    @Test
    public void testGetGameToTag_Success() {
        // Arrange
        String gameToTagId = "gameToTag789";
        GameToTag mockGameToTag = new GameToTag(new Game().getId(), new Tag().getId());

        when(gameToTagService.getGameToTagById(gameToTagId)).thenReturn(mockGameToTag);

        // Act
        ResponseEntity<GameToTag> response = gameToTagController.getGameToTag(gameToTagId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockGameToTag, response.getBody());
        verify(gameToTagService, times(1)).getGameToTagById(gameToTagId);
    }

    /**
     * Test the getGameToTag endpoint when the GameToTag is not found.
     */
    @Test
    public void testGetGameToTag_NotFound() {
        // Arrange
        String gameToTagId = "nonexistentGameToTag";

        when(gameToTagService.getGameToTagById(gameToTagId))
                .thenThrow(new IllegalArgumentException("GameToTag not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameToTagController.getGameToTag(gameToTagId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("GameToTag not found.", exception.getReason());
        verify(gameToTagService, times(1)).getGameToTagById(gameToTagId);
    }

    /**
     * Test the getAllGameToTags endpoint for a successful retrieval of all GameToTags.
     */
    @Test
    public void testGetAllGameToTags_Success() {
        // Arrange
    GameToTag gameToTag1 = new GameToTag(new Game().getId(), new Tag().getId());
        GameToTag gameToTag2 = new GameToTag(new Game().getId(), new Tag().getId());
        List<GameToTag> mockGameToTags = Arrays.asList(gameToTag1, gameToTag2);

        when(gameToTagService.getAllGameToTags()).thenReturn(mockGameToTags);

        // Act
        ResponseEntity<List<GameToTag>> response = gameToTagController.getAllGameToTags();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockGameToTags, response.getBody());
        verify(gameToTagService, times(1)).getAllGameToTags();
    }

    /**
     * Test the getAllGameToTags endpoint when an exception occurs.
     */
    @Test
    public void testGetAllGameToTags_Failure() {
        // Arrange
        when(gameToTagService.getAllGameToTags())
                .thenThrow(new IllegalArgumentException("Unable to retrieve GameToTags."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameToTagController.getAllGameToTags();
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Unable to retrieve GameToTags.", exception.getReason());
        verify(gameToTagService, times(1)).getAllGameToTags();
    }

    /**
     * Test the deleteGameToTag endpoint for a successful deletion of a GameToTag.
     */
    @Test
    public void testDeleteGameToTag_Success() {
        // Arrange
        String gameToTagId = "gameToTag789";

        doNothing().when(gameToTagService).deleteGameToTagById(gameToTagId);

        // Act
        ResponseEntity response = gameToTagController.deleteGameToTag(gameToTagId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gameToTagService, times(1)).deleteGameToTagById(gameToTagId);
    }

    /**
     * Test the deleteGameToTag endpoint when the GameToTag does not exist.
     */
    @Test
    public void testDeleteGameToTag_NotFound() {
        // Arrange
        String gameToTagId = "nonexistentGameToTag";

        doThrow(new IllegalArgumentException("GameToTag not found."))
                .when(gameToTagService).deleteGameToTagById(gameToTagId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameToTagController.deleteGameToTag(gameToTagId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("GameToTag not found.", exception.getReason());
        verify(gameToTagService, times(1)).deleteGameToTagById(gameToTagId);
    }
}
