package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.domain.Game;
import org.gamed.gamelistdatabaseservice.repository.GameRepository;
import org.gamed.gamelistdatabaseservice.service.GameService;
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
public class GameServiceIntegrationTests {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void testCreateGameSuccess() {
        String name = "Test Game";
        String developer = "Test Developer";
        LocalDateTime releaseDate = LocalDateTime.now();
        String platforms = "PC";

        when(gameRepository.existsByName(name)).thenReturn(false);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game game = gameService.createGame(name, developer, releaseDate, platforms);

        assertNotNull(game);
        assertEquals(name, game.getName());
        assertEquals(developer, game.getDeveloper());
        assertEquals(releaseDate, game.getReleaseDate());
        assertEquals(platforms, game.getPlatforms());

        verify(gameRepository).existsByName(name);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void testCreateGameThrowsExceptionWhenNameIsBlank() {
        String name = "";
        String developer = "Test Developer";
        LocalDateTime releaseDate = LocalDateTime.now();
        String platforms = "PC";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(name, developer, releaseDate, platforms)
        );

        assertEquals("Game name cannot be blank.", exception.getMessage());
        verify(gameRepository, never()).existsByName(anyString());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void testCreateGameThrowsExceptionWhenGameAlreadyExists() {
        String name = "Test Game";
        String developer = "Test Developer";
        LocalDateTime releaseDate = LocalDateTime.now();
        String platforms = "PC";

        when(gameRepository.existsByName(name)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(name, developer, releaseDate, platforms)
        );

        assertEquals("Game already exists.", exception.getMessage());
        verify(gameRepository).existsByName(name);
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void testGetGameSuccess() {
        String id = "gameId";
        Game game = new Game("Test Game", "Test Developer", LocalDateTime.now(), "PC");
        when(gameRepository.findById(id)).thenReturn(Optional.of(game));

        Game result = gameService.getGame(id);

        assertNotNull(result);
        assertEquals(game, result);
        verify(gameRepository).findById(id);
    }

    @Test
    void testGetGameThrowsExceptionWhenGameNotFound() {
        String id = "gameId";
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGame(id)
        );

        assertEquals("Given Game id does not correspond to any Game. ID: " + id, exception.getMessage());
        verify(gameRepository).findById(id);
    }

    @Test
    void testUpdateGameSuccess() {
        String id = "gameId";
        Game existingGame = new Game("Test Game", "Test Developer", LocalDateTime.now(), "PC");
        when(gameRepository.findById(id)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime releaseDate = null; // Assuming we don't update release date
        String platforms = "Xbox";

        Game updatedGame = gameService.updateGame(id, releaseDate, platforms);

        assertNotNull(updatedGame);
        assertEquals("Xbox", updatedGame.getPlatforms());
        verify(gameRepository).findById(id);
        verify(gameRepository).save(existingGame);
    }

    @Test
    void testUpdateGameThrowsExceptionWhenReleaseDateIsNotNull() {
        String id = "gameId";
        Game existingGame = new Game("Test Game", "Test Developer", LocalDateTime.now(), "PC");
        when(gameRepository.findById(id)).thenReturn(Optional.of(existingGame));

        LocalDateTime newReleaseDate = LocalDateTime.now().plusDays(1);
        String platforms = "PC";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameService.updateGame(id, newReleaseDate, platforms)
        );

        assertEquals("Release date cannot be blank.", exception.getMessage());
        verify(gameRepository).findById(id);
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void testUpdateGameThrowsExceptionWhenPlatformsIsEmpty() {
        String id = "gameId";
        Game existingGame = new Game("Test Game", "Test Developer", LocalDateTime.now(), "PC");
        when(gameRepository.findById(id)).thenReturn(Optional.of(existingGame));

        LocalDateTime releaseDate = null;
        String platforms = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameService.updateGame(id, releaseDate, platforms)
        );

        assertEquals("Platforms cannot be blank.", exception.getMessage());
        verify(gameRepository).findById(id);
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void testDeleteGameSuccess() {
        String id = "gameId";
        when(gameRepository.existsById(id)).thenReturn(true);

        gameService.deleteGame(id);

        verify(gameRepository).existsById(id);
        verify(gameRepository).deleteById(id);
    }

    @Test
    void testDeleteGameThrowsExceptionWhenGameNotFound() {
        String id = "gameId";
        when(gameRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameService.deleteGame(id)
        );

        assertEquals("Given Game id does not correspond to any Game. ID: " + id, exception.getMessage());
        verify(gameRepository).existsById(id);
        verify(gameRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetAllGames() {
        List<Game> games = Arrays.asList(
                new Game("Game1", "Dev1", LocalDateTime.now(), "PC"),
                new Game("Game2", "Dev2", LocalDateTime.now(), "Xbox")
        );

        when(gameRepository.findAll()).thenReturn(games);

        List<Game> result = gameService.getAllGames();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(games, result);

        verify(gameRepository).findAll();
    }

    @Test
    void testGetGamesByDeveloper() {
        String developer = "Test Developer";
        List<Game> games = Arrays.asList(
                new Game("Game1", developer, LocalDateTime.now(), "PC"),
                new Game("Game2", developer, LocalDateTime.now(), "Xbox")
        );

        when(gameRepository.getGamesByDeveloper(developer)).thenReturn(games);

        List<Game> result = gameService.getGamesByDeveloper(developer);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(games, result);

        verify(gameRepository).getGamesByDeveloper(developer);
    }

    @Test
    void testGetGamesByReleaseDate() {
        String releaseDate = "2023-01-01";
        List<Game> games = Arrays.asList(
                new Game("Game1", "Dev1", LocalDateTime.parse("2023-01-01T00:00:00"), "PC"),
                new Game("Game2", "Dev2", LocalDateTime.parse("2023-01-01T00:00:00"), "Xbox")
        );

        when(gameRepository.getGamesByReleaseDate(releaseDate)).thenReturn(games);

        List<Game> result = gameService.getGamesByReleaseDate(releaseDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(games, result);

        verify(gameRepository).getGamesByReleaseDate(releaseDate);
    }

    @Test
    void testGetGamesByPlatform() {
        String platform = "PC";
        List<Game> games = Arrays.asList(
                new Game("Game1", "Dev1", LocalDateTime.now(), platform),
                new Game("Game2", "Dev2", LocalDateTime.now(), platform)
        );

        when(gameRepository.getGamesByPlatform(platform)).thenReturn(games);

        List<Game> result = gameService.getGamesByPlatform(platform);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(games, result);

        verify(gameRepository).getGamesByPlatform(platform);
    }

    @Test
    void testGetGamesByQuery() {
        String query = "Test";
        List<Game> games = Arrays.asList(
                new Game("Test Game1", "Dev1", LocalDateTime.now(), "PC"),
                new Game("Game2", "Test Dev", LocalDateTime.now(), "Xbox")
        );

        when(gameRepository.getGamesByQuery(query)).thenReturn(games);

        List<Game> result = gameService.getGamesByQuery(query);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(games, result);

        verify(gameRepository).getGamesByQuery(query);
    }
}
