package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.controllers.GameController;
import org.gamed.gamelistdatabaseservice.domain.Game;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateGameRequestModel;
import org.gamed.gamelistdatabaseservice.service.GameService;
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
 * Test class for GameController.
 */
public class GameControllerIntegrationTests {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createGame endpoint for a successful game creation.
     */
    @Test
    public void testCreateGame_Success() {
        // Arrange
        CreateAndUpdateGameRequestModel request = new CreateAndUpdateGameRequestModel(
                "Cyberpunk 2077",
                "CD Projekt",
                LocalDateTime.of(12,2,1,0,0),
               "PC, PS4"
        );

        Game mockGame = new Game("Cyberpunk 2077", "CD Projekt",  LocalDateTime.of(12,2,1,0,0), "PC, PS4");

        when(gameService.createGame(
                request.getName(),
                request.getDeveloper(),
                request.getReleaseDate(),
                request.getPlatforms()
        )).thenReturn(mockGame);

        // Act
        ResponseEntity<String> response = gameController.createGame(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(gameService, times(1)).createGame(
                request.getName(),
                request.getDeveloper(),
                request.getReleaseDate(),
                request.getPlatforms()
        );
    }

    /**
     * Test the createGame endpoint when an exception occurs.
     */
    @Test
    public void testCreateGame_Failure() {
        // Arrange
        CreateAndUpdateGameRequestModel request = new CreateAndUpdateGameRequestModel(
                "Cyberpunk 2077",
                "CD Projekt",
                LocalDateTime.of(12,2,1,0,0),
                "PC, PS4"
        );

        when(gameService.createGame(
                request.getName(),
                request.getDeveloper(),
                request.getReleaseDate(),
                request.getPlatforms()
        )).thenThrow(new IllegalArgumentException("Invalid game data."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameController.createGame(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid game data.", exception.getReason());
        verify(gameService, times(1)).createGame(
                request.getName(),
                request.getDeveloper(),
                request.getReleaseDate(),
                request.getPlatforms()
        );
    }

    /**
     * Test the updateGame endpoint for a successful game update.
     */
    @Test
    public void testUpdateGame_Success() {
        // Arrange
        String gameId = "game123";
        CreateAndUpdateGameRequestModel request = new CreateAndUpdateGameRequestModel(
                "Cyberpunk 2077",
                "CD Projekt Red",
                LocalDateTime.of(12,2,1,0,0),
                "PC, PS4"
        );

        when(gameService.updateGame(
                gameId,
                request.getReleaseDate(),
                request.getPlatforms()
        )).thenReturn(new Game());

        // Act
        ResponseEntity response = gameController.updateGame(gameId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gameService, times(1)).updateGame(
                gameId,
                request.getReleaseDate(),
                request.getPlatforms()
        );
    }

    /**
     * Test the updateGame endpoint when the game does not exist.
     */
    @Test
    public void testUpdateGame_GameNotFound() {
        // Arrange
        String gameId = "nonexistentGame";
        CreateAndUpdateGameRequestModel request = new CreateAndUpdateGameRequestModel(
                "Cyberpunk 2077",
                "CD Projekt Red",
                LocalDateTime.of(12,2,1,0,0),
                "PC, PS4"
        );

        doThrow(new IllegalArgumentException("Game not found."))
                .when(gameService).updateGame(
                        gameId,
                        request.getReleaseDate(),
                        request.getPlatforms()
                );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameController.updateGame(gameId, request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Game not found.", exception.getReason());
        verify(gameService, times(1)).updateGame(
                gameId,
                request.getReleaseDate(),
                request.getPlatforms()
        );
    }

    /**
     * Test the getGame endpoint for a successful retrieval of a game.
     */
    @Test
    public void testGetGame_Success() {
        // Arrange
        String gameId = "game123";
        Game mockGame = new Game("Cyberpunk 2077", "CD Projekt", LocalDateTime.of(12,2,1,0,0),"PC, PS4");

        when(gameService.getGame(gameId)).thenReturn(mockGame);

        // Act
        ResponseEntity<Game> response = gameController.getGame(gameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockGame, response.getBody());
        verify(gameService, times(1)).getGame(gameId);
    }

    /**
     * Test the getGame endpoint when the game is not found.
     */
    @Test
    public void testGetGame_NotFound() {
        // Arrange
        String gameId = "nonexistentGame";

        when(gameService.getGame(gameId))
                .thenThrow(new IllegalArgumentException("Game not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameController.getGame(gameId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Game not found.", exception.getReason());
        verify(gameService, times(1)).getGame(gameId);
    }

    /**
     * Test the getAllGames endpoint for a successful retrieval of all games.
     */
    @Test
    public void testGetAllGames_Success() {
        // Arrange
        Game game1 = new Game("Cyberpunk 2077", "CD Projekt", LocalDateTime.of(12,2,1,0,0), "PC, PS4");
        Game game2 = new Game("The Witcher 3", "CD Projekt", LocalDateTime.of(12,2,1,0,0), "PC, PS4");
        List<Game> mockGames = Arrays.asList(game1, game2);

        when(gameService.getAllGames()).thenReturn(mockGames);

        // Act
        ResponseEntity<List<Game>> response = gameController.getAllGames();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockGames, response.getBody());
        verify(gameService, times(1)).getAllGames();
    }

    /**
     * Test the getAllGames endpoint when an exception occurs.
     */
    @Test
    public void testGetAllGames_Failure() {
        // Arrange
        when(gameService.getAllGames())
                .thenThrow(new IllegalArgumentException("Unable to retrieve games."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameController.getAllGames();
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Unable to retrieve games.", exception.getReason());
        verify(gameService, times(1)).getAllGames();
    }

    /**
     * Test the deleteGame endpoint for a successful deletion of a game.
     */
    @Test
    public void testDeleteGame_Success() {
        // Arrange
        String gameId = "game123";

        doNothing().when(gameService).deleteGame(gameId);

        // Act
        ResponseEntity response = gameController.deleteGame(gameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gameService, times(1)).deleteGame(gameId);
    }

    /**
     * Test the deleteGame endpoint when the game does not exist.
     */
    @Test
    public void testDeleteGame_GameNotFound() {
        // Arrange
        String gameId = "nonexistentGame";

        doThrow(new IllegalArgumentException("Game not found."))
                .when(gameService).deleteGame(gameId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameController.deleteGame(gameId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Game not found.", exception.getReason());
        verify(gameService, times(1)).deleteGame(gameId);
    }
}
