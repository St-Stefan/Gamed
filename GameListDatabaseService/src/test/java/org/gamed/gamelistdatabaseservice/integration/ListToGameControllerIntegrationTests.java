package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.controllers.ListToGameController;
import org.gamed.gamelistdatabaseservice.domain.Game;
import org.gamed.gamelistdatabaseservice.domain.GameList;
import org.gamed.gamelistdatabaseservice.domain.ListToGame;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateListToGameRequestModel;
import org.gamed.gamelistdatabaseservice.service.ListToGameService;
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
 * Test class for ListToGameController.
 */
public class ListToGameControllerIntegrationTests {

    @Mock
    private ListToGameService listToGameService;

    @InjectMocks
    private ListToGameController listToGameController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createListToGame endpoint for a successful ListToGame creation.
     */
    @Test
    public void testCreateListToGame_Success() {
        // Arrange
        CreateAndUpdateListToGameRequestModel request = new CreateAndUpdateListToGameRequestModel(
                "list123",
                "game456"
        );

        ListToGame mockListToGame = new ListToGame(new GameList().getId(), new Game().getId());

        when(listToGameService.createListToGame(
                request.getListId(),
                request.getGameId()
        )).thenReturn(mockListToGame);

        // Act
        ResponseEntity<String> response = listToGameController.createListToGame(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(listToGameService, times(1)).createListToGame(
                request.getListId(),
                request.getGameId()
        );
    }

    /**
     * Test the createListToGame endpoint when an exception occurs.
     */
    @Test
    public void testCreateListToGame_Failure() {
        // Arrange
        CreateAndUpdateListToGameRequestModel request = new CreateAndUpdateListToGameRequestModel(
                "list123",
                "game456"
        );

        when(listToGameService.createListToGame(
                request.getListId(),
                request.getGameId()
        )).thenThrow(new IllegalArgumentException("Invalid List or Game ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            listToGameController.createListToGame(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid List or Game ID.", exception.getReason());
        verify(listToGameService, times(1)).createListToGame(
                request.getListId(),
                request.getGameId()
        );
    }

    /**
     * Test the getListToGame endpoint for a successful retrieval of a ListToGame.
     */
    @Test
    public void testGetListToGame_Success() {
        // Arrange
        String listToGameId = "listToGame789";
        ListToGame mockListToGame = new ListToGame(new GameList().getId(), new Game().getId());

        when(listToGameService.getListToGameById(listToGameId)).thenReturn(mockListToGame);

        // Act
        ResponseEntity<ListToGame> response = listToGameController.getListToGame(listToGameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockListToGame, response.getBody());
        verify(listToGameService, times(1)).getListToGameById(listToGameId);
    }

    /**
     * Test the getListToGame endpoint when the ListToGame is not found.
     */
    @Test
    public void testGetListToGame_NotFound() {
        // Arrange
        String listToGameId = "nonexistentListToGame";

        when(listToGameService.getListToGameById(listToGameId))
                .thenThrow(new IllegalArgumentException("ListToGame not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            listToGameController.getListToGame(listToGameId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("ListToGame not found.", exception.getReason());
        verify(listToGameService, times(1)).getListToGameById(listToGameId);
    }

    /**
     * Test the getAllListToGames endpoint for a successful retrieval of all ListToGames.
     */
    @Test
    public void testGetAllListToGames_Success() {
        // Arrange
        ListToGame listToGame1 = new ListToGame(new GameList().getId(), new Game().getId());
        ListToGame listToGame2 = new ListToGame(new GameList().getId(), new Game().getId());
        List<ListToGame> mockListToGames = Arrays.asList(listToGame1, listToGame2);

        when(listToGameService.getAllListToGames()).thenReturn(mockListToGames);

        // Act
        ResponseEntity<List<ListToGame>> response = listToGameController.getAllListToGames();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockListToGames, response.getBody());
        verify(listToGameService, times(1)).getAllListToGames();
    }

    /**
     * Test the getAllListToGames endpoint when an exception occurs.
     */
    @Test
    public void testGetAllListToGames_Failure() {
        // Arrange
        when(listToGameService.getAllListToGames())
                .thenThrow(new IllegalArgumentException("Unable to retrieve ListToGames."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            listToGameController.getAllListToGames();
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Unable to retrieve ListToGames.", exception.getReason());
        verify(listToGameService, times(1)).getAllListToGames();
    }

    /**
     * Test the deleteListToGame endpoint for a successful deletion of a ListToGame.
     */
    @Test
    public void testDeleteListToGame_Success() {
        // Arrange
        String listToGameId = "listToGame789";

        doNothing().when(listToGameService).deleteListToGameById(listToGameId);

        // Act
        ResponseEntity response = listToGameController.deleteListToGame(listToGameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listToGameService, times(1)).deleteListToGameById(listToGameId);
    }

    /**
     * Test the deleteListToGame endpoint when the ListToGame does not exist.
     */
    @Test
    public void testDeleteListToGame_NotFound() {
        // Arrange
        String listToGameId = "nonexistentListToGame";

        doThrow(new IllegalArgumentException("ListToGame not found."))
                .when(listToGameService).deleteListToGameById(listToGameId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            listToGameController.deleteListToGame(listToGameId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("ListToGame not found.", exception.getReason());
        verify(listToGameService, times(1)).deleteListToGameById(listToGameId);
    }
}
