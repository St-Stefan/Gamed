package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.domain.GameList;
import org.gamed.gamelistdatabaseservice.repository.GameListRepository;
import org.gamed.gamelistdatabaseservice.service.GameListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameListServiceIntegrationTests {

    @Mock
    private GameListRepository gameListRepository;

    @InjectMocks
    private GameListService gameListService;

    @Test
    void testCreateListSuccess() {
        String userId = "user123";
        String name = "My Game List";
        String description = "A list of my favorite games";

        when(gameListRepository.existsByUserIdAndName(userId, name)).thenReturn(false);
        when(gameListRepository.save(any(GameList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GameList createdList = gameListService.createList(userId, name, description);

        assertNotNull(createdList);
        assertEquals(userId, createdList.getUserId());
        assertEquals(name, createdList.getName());
        assertEquals(description, createdList.getDescription());

        verify(gameListRepository).existsByUserIdAndName(userId, name);
        verify(gameListRepository).save(any(GameList.class));
    }

    @Test
    void testCreateListThrowsExceptionForExistingList() {
        String userId = "user123";
        String name = "My Game List";
        String description = "A list of my favorite games";

        when(gameListRepository.existsByUserIdAndName(userId, name)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameListService.createList(userId, name, description)
        );

        assertEquals("A list with this name already exists for this user.", exception.getMessage());

        verify(gameListRepository).existsByUserIdAndName(userId, name);
        verify(gameListRepository, never()).save(any(GameList.class));
    }

    @Test
    void testGetListSuccess() {
        String listId = "list123";
        GameList gameList = new GameList("user123", "My Game List", "Description");
        when(gameListRepository.findById(listId)).thenReturn(Optional.of(gameList));

        GameList result = gameListService.getList(listId);

        assertNotNull(result);
        assertEquals(gameList, result);

        verify(gameListRepository).findById(listId);
    }

    @Test
    void testGetListThrowsExceptionForNonExistingList() {
        String listId = "list123";
        when(gameListRepository.findById(listId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameListService.getList(listId)
        );

        assertEquals("Given List id does not correspond to any List. ID: " + listId, exception.getMessage());

        verify(gameListRepository).findById(listId);
    }

    @Test
    void testUpdateListSuccess() {
        String listId = "list123";
        String newName = "Updated Game List";
        String newDescription = "Updated Description";
        GameList existingList = new GameList("user123", "My Game List", "Description");

        when(gameListRepository.findById(listId)).thenReturn(Optional.of(existingList));
        when(gameListRepository.save(any(GameList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GameList updatedList = gameListService.updateList(listId, newName, newDescription);

        assertNotNull(updatedList);
        assertEquals(newName, updatedList.getName());
        assertEquals(newDescription, updatedList.getDescription());

        verify(gameListRepository).findById(listId);
        verify(gameListRepository).save(existingList);
    }

    @Test
    void testUpdateListThrowsExceptionForEmptyName() {
        String listId = "list123";
        String newName = "";
        String newDescription = "Updated Description";
        GameList existingList = new GameList("user123", "My Game List", "Description");

        when(gameListRepository.findById(listId)).thenReturn(Optional.of(existingList));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameListService.updateList(listId, newName, newDescription)
        );

        assertEquals("List name cannot be blank.", exception.getMessage());

        verify(gameListRepository).findById(listId);
        verify(gameListRepository, never()).save(any(GameList.class));
    }

    @Test
    void testDeleteListSuccess() {
        String listId = "list123";

        when(gameListRepository.existsById(listId)).thenReturn(true);

        gameListService.deleteList(listId);

        verify(gameListRepository).existsById(listId);
        verify(gameListRepository).deleteById(listId);
    }

    @Test
    void testDeleteListThrowsExceptionForNonExistingList() {
        String listId = "list123";

        when(gameListRepository.existsById(listId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameListService.deleteList(listId)
        );

        assertEquals("Given List id does not correspond to any List. ID: " + listId, exception.getMessage());

        verify(gameListRepository).existsById(listId);
        verify(gameListRepository, never()).deleteById(listId);
    }


    @Test
    void testGetListsByUserId() {
        String userId = "user123";
        List<GameList> gameLists = List.of(
                new GameList(userId, "List1", "Description1"),
                new GameList(userId, "List2", "Description2")
        );

        when(gameListRepository.getListsByUserId(userId)).thenReturn(gameLists);

        List<GameList> result = gameListService.getListsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(gameLists, result);

        verify(gameListRepository).getListsByUserId(userId);
    }

    @Test
    void testGetListByUserIdAndNameSuccess() {
        String userId = "user123";
        String name = "My Game List";
        GameList gameList = new GameList(userId, name, "Description");

        when(gameListRepository.getListByUserIdAndName(userId, name)).thenReturn(gameList);

        GameList result = gameListService.getListByUserIdAndName(userId, name);

        assertNotNull(result);
        assertEquals(gameList, result);

        verify(gameListRepository).getListByUserIdAndName(userId, name);
    }

    @Test
    void testGetListByUserIdAndNameThrowsExceptionForNonExistingList() {
        String userId = "user123";
        String name = "Non-existing List";

        when(gameListRepository.getListByUserIdAndName(userId, name)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameListService.getListByUserIdAndName(userId, name)
        );

        assertEquals("No list found with the user ID: " + userId + " and name: " + name, exception.getMessage());

        verify(gameListRepository).getListByUserIdAndName(userId, name);
    }

    @Test
    void testGetAllLists() {
        List<GameList> gameLists = List.of(
                new GameList("user1", "List1", "Description1"),
                new GameList("user2", "List2", "Description2")
        );

        when(gameListRepository.findAll()).thenReturn(gameLists);

        List<GameList> result = gameListService.getAllLists();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(gameLists, result);

        verify(gameListRepository).findAll();
    }
}
