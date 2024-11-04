package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.controllers.GameListController;
import org.gamed.gamelistdatabaseservice.domain.GameList;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateListRequestModel;
import org.gamed.gamelistdatabaseservice.service.GameListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for GameListController.
 */
public class GameListControllerIntegrationTests {

    @Mock
    private GameListService gameListService;

    @InjectMocks
    private GameListController gameListController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createList endpoint for a successful list creation.
     */
    @Test
    public void testCreateList_Success() {
        // Arrange
        CreateAndUpdateListRequestModel request = new CreateAndUpdateListRequestModel(
                "user123",
                "Favorites",
                "My favorite games."
        );

        GameList mockList = new GameList("user123", "Favorites", "My favorite games.");

        when(gameListService.createList(
                request.getUserId(),
                request.getName(),
                request.getDescription()
        )).thenReturn(mockList);

        // Act
        ResponseEntity<String> response = gameListController.createList(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(gameListService, times(1)).createList(
                request.getUserId(),
                request.getName(),
                request.getDescription()
        );
    }

    /**
     * Test the createList endpoint when an exception occurs.
     */
    @Test
    public void testCreateList_Failure() {
        // Arrange
        CreateAndUpdateListRequestModel request = new CreateAndUpdateListRequestModel(
                "user123",
                "Favorites",
                "My favorite games."
        );

        when(gameListService.createList(
                request.getUserId(),
                request.getName(),
                request.getDescription()
        )).thenThrow(new IllegalArgumentException("Invalid list data."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameListController.createList(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid list data.", exception.getReason());
        verify(gameListService, times(1)).createList(
                request.getUserId(),
                request.getName(),
                request.getDescription()
        );
    }

    /**
     * Test the updateList endpoint for a successful list update.
     */
    @Test
    public void testUpdateList_Success() {
        // Arrange
        String listId = "list456";
        CreateAndUpdateListRequestModel request = new CreateAndUpdateListRequestModel(
                "user123",
                "Favorites Updated",
                "Updated description."
        );

        when(gameListService.updateList(
                listId,
                request.getName(),
                request.getUserId()
        )).thenReturn(new GameList());

        // Act
        ResponseEntity response = gameListController.updateList(listId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gameListService, times(1)).updateList(
                listId,
                request.getName(),
                request.getUserId()
        );
    }

    /**
     * Test the updateList endpoint when the list does not exist.
     */
    @Test
    public void testUpdateList_ListNotFound() {
        // Arrange
        String listId = "nonexistentList";
        CreateAndUpdateListRequestModel request = new CreateAndUpdateListRequestModel(
                "user123",
                "Favorites Updated",
                "Updated description."
        );

        doThrow(new IllegalArgumentException("List not found."))
                .when(gameListService).updateList(
                        listId,
                        request.getName(),
                        request.getUserId()
                );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameListController.updateList(listId, request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("List not found.", exception.getReason());
        verify(gameListService, times(1)).updateList(
                listId,
                request.getName(),
                request.getUserId()
        );
    }

    /**
     * Test the getList endpoint for a successful retrieval of a list.
     */
    @Test
    public void testGetList_Success() {
        // Arrange
        String listId = "list456";
        GameList mockList = new GameList("user123", "Favorites", "My favorite games.");

        when(gameListService.getList(listId)).thenReturn(mockList);

        // Act
        ResponseEntity<GameList> response = gameListController.getList(listId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockList, response.getBody());
        verify(gameListService, times(1)).getList(listId);
    }

    /**
     * Test the getList endpoint when the list is not found.
     */
    @Test
    public void testGetList_NotFound() {
        // Arrange
        String listId = "nonexistentList";

        when(gameListService.getList(listId))
                .thenThrow(new IllegalArgumentException("List not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameListController.getList(listId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("List not found.", exception.getReason());
        verify(gameListService, times(1)).getList(listId);
    }

    /**
     * Test the deleteList endpoint for a successful deletion of a list.
     */
    @Test
    public void testDeleteList_Success() {
        // Arrange
        String listId = "list456";

        doNothing().when(gameListService).deleteList(listId);

        // Act
        ResponseEntity response = gameListController.deleteList(listId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gameListService, times(1)).deleteList(listId);
    }

    /**
     * Test the deleteList endpoint when the list does not exist.
     */
    @Test
    public void testDeleteList_ListNotFound() {
        // Arrange
        String listId = "nonexistentList";

        doThrow(new IllegalArgumentException("List not found."))
                .when(gameListService).deleteList(listId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameListController.deleteList(listId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("List not found.", exception.getReason());
        verify(gameListService, times(1)).deleteList(listId);
    }
}
