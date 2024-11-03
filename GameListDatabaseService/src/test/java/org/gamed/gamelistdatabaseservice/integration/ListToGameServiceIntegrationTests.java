package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.domain.ListToGame;
import org.gamed.gamelistdatabaseservice.repository.ListToGameRepository;
import org.gamed.gamelistdatabaseservice.service.GameListService;
import org.gamed.gamelistdatabaseservice.service.GameService;
import org.gamed.gamelistdatabaseservice.service.ListToGameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListToGameServiceIntegrationTests {

    @Mock
    private ListToGameRepository listToGameRepository;

    @Mock
    private GameService gameService;

    @Mock
    private GameListService gameListService;

    @InjectMocks
    private ListToGameService listToGameService;

    @Test
    void testCreateListToGameSuccess() {
        String listId = "listId123";
        String gameId = "gameId123";

        when(listToGameRepository.existsByListIdAndGameId(listId, gameId)).thenReturn(false);
        when(listToGameRepository.save(any(ListToGame.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ListToGame result = listToGameService.createListToGame(listId, gameId);

        assertNotNull(result);
        assertEquals(listId, result.getList());
        assertEquals(gameId, result.getGame());

        verify(listToGameRepository).existsByListIdAndGameId(listId, gameId);
        verify(listToGameRepository).save(any(ListToGame.class));
    }

    @Test
    void testCreateListToGameThrowsExceptionWhenListIdIsNull() {
        String listId = null;
        String gameId = "gameId123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToGameService.createListToGame(listId, gameId)
        );

        assertEquals("Ids cannot be null.", exception.getMessage());
        verify(listToGameRepository, never()).existsByListIdAndGameId(anyString(), anyString());
        verify(listToGameRepository, never()).save(any(ListToGame.class));
    }

    @Test
    void testCreateListToGameThrowsExceptionWhenGameIdIsNull() {
        String listId = "listId123";
        String gameId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToGameService.createListToGame(listId, gameId)
        );

        assertEquals("Ids cannot be null.", exception.getMessage());
        verify(listToGameRepository, never()).existsByListIdAndGameId(anyString(), anyString());
        verify(listToGameRepository, never()).save(any(ListToGame.class));
    }

    @Test
    void testCreateListToGameThrowsExceptionWhenRelationAlreadyExists() {
        String listId = "listId123";
        String gameId = "gameId123";

        when(listToGameRepository.existsByListIdAndGameId(listId, gameId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToGameService.createListToGame(listId, gameId)
        );

        assertEquals("Relation already exists.", exception.getMessage());
        verify(listToGameRepository).existsByListIdAndGameId(listId, gameId);
        verify(listToGameRepository, never()).save(any(ListToGame.class));
    }

    @Test
    void testGetListToGameByIdThrowsExceptionWhenNotFound() {
        String id = "listToGameId123";

        when(listToGameRepository.getListToGameById(id)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToGameService.getListToGameById(id)
        );

        assertEquals("Given ListToGame id does not correspond to any ListToGame. ID: " + id, exception.getMessage());
        verify(listToGameRepository).getListToGameById(id);
    }

    @Test
    void testGetListToGamesByListId() {
        String listId = "listId123";
        List<ListToGame> listToGames = Arrays.asList(
                new ListToGame(listId, "gameId1"),
                new ListToGame(listId, "gameId2")
        );

        when(listToGameRepository.getListToGamesByListId(listId)).thenReturn(listToGames);

        List<ListToGame> result = listToGameService.getListToGamesByListId(listId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(listToGames, result);
        verify(listToGameRepository).getListToGamesByListId(listId);
    }

    @Test
    void testGetListToGamesByGameId() {
        String gameId = "gameId123";
        List<ListToGame> listToGames = Arrays.asList(
                new ListToGame("listId1", gameId),
                new ListToGame("listId2", gameId)
        );

        when(listToGameRepository.getListToGamesByGameId(gameId)).thenReturn(listToGames);

        List<ListToGame> result = listToGameService.getListToGamesByGameId(gameId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(listToGames, result);
        verify(listToGameRepository).getListToGamesByGameId(gameId);
    }

    @Test
    void testGetListToGameByListIdAndGameIdSuccess() {
        String listId = "listId123";
        String gameId = "gameId123";
        ListToGame listToGame = new ListToGame(listId, gameId);

        when(listToGameRepository.getListToGameByListIdAndGameId(listId, gameId)).thenReturn(listToGame);

        ListToGame result = listToGameService.getListToGameByListIdAndGameId(listId, gameId);

        assertNotNull(result);
        assertEquals(listToGame, result);
        verify(listToGameRepository).getListToGameByListIdAndGameId(listId, gameId);
    }

    @Test
    void testGetListToGameByListIdAndGameIdThrowsExceptionWhenNotFound() {
        String listId = "listId123";
        String gameId = "gameId123";

        when(listToGameRepository.getListToGameByListIdAndGameId(listId, gameId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToGameService.getListToGameByListIdAndGameId(listId, gameId)
        );

        assertEquals("No ListToGame found for List ID: " + listId + " and Game ID: " + gameId, exception.getMessage());
        verify(listToGameRepository).getListToGameByListIdAndGameId(listId, gameId);
    }

    @Test
    void testDeleteListToGameByIdSuccess() {
        String id = "listToGameId123";

        when(listToGameRepository.existsById(id)).thenReturn(true);

        listToGameService.deleteListToGameById(id);

        verify(listToGameRepository).existsById(id);
        verify(listToGameRepository).deleteListToGameById(id);
    }

    @Test
    void testDeleteListToGameByIdThrowsExceptionWhenNotFound() {
        String id = "listToGameId123";

        when(listToGameRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToGameService.deleteListToGameById(id)
        );

        assertEquals("Given ListToGame id does not correspond to any ListToGame. ID: " + id, exception.getMessage());
        verify(listToGameRepository).existsById(id);
        verify(listToGameRepository, never()).deleteListToGameById(anyString());
    }

    @Test
    void testExistsByListIdAndGameId() {
        String listId = "listId123";
        String gameId = "gameId123";

        when(listToGameRepository.existsByListIdAndGameId(listId, gameId)).thenReturn(true);

        boolean result = listToGameService.existsByListIdAndGameId(listId, gameId);

        assertTrue(result);
        verify(listToGameRepository).existsByListIdAndGameId(listId, gameId);
    }

    @Test
    void testGetAllListToGames() {
        List<ListToGame> listToGames = Arrays.asList(
                new ListToGame("listId1", "gameId1"),
                new ListToGame("listId2", "gameId2")
        );

        when(listToGameRepository.findAll()).thenReturn(listToGames);

        List<ListToGame> result = listToGameService.getAllListToGames();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(listToGames, result);
        verify(listToGameRepository).findAll();
    }
}
