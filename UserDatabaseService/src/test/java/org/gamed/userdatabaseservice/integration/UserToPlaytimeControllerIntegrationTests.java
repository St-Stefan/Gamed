package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.controllers.UserToPlaytimeController;
import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToPlaytime;
import org.gamed.userdatabaseservice.service.UserToPlaytimeService;
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
 * Test class for UserToPlaytimeController.
 */
public class UserToPlaytimeControllerIntegrationTests {

    @Mock
    private UserToPlaytimeService playtimeService;

    @InjectMocks
    private UserToPlaytimeController playtimeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the recordPlaytime endpoint for a successful playtime recording.
     */
    @Test
    public void testRecordPlaytime_Success() {
        // Arrange
        String userId = "user123";
        String gameId = "game456";
        int playtime = 120; // minutes

        when(playtimeService.createPlaytime(userId, gameId, playtime)).thenReturn(new UserToPlaytime());

        // Act
        ResponseEntity<String> response = playtimeController.recordPlaytime(userId, gameId, playtime);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(playtimeService, times(1)).createPlaytime(userId, gameId, playtime);
    }

    /**
     * Test the recordPlaytime endpoint when an exception occurs.
     */
    @Test
    public void testRecordPlaytime_Failure() {
        // Arrange
        String userId = "user123";
        String gameId = "game456";
        int playtime = 120; // minutes

        doThrow(new IllegalArgumentException("Unable to record playtime."))
                .when(playtimeService).createPlaytime(userId, gameId, playtime);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            playtimeController.recordPlaytime(userId, gameId, playtime);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Unable to record playtime.", exception.getReason());
        verify(playtimeService, times(1)).createPlaytime(userId, gameId, playtime);
    }

    /**
     * Test the updatePlaytime endpoint for a successful playtime update.
     */
    @Test
    public void testUpdatePlaytime_Success() {
        // Arrange
        String userId = "user123";
        String gameId = "game456";
        int newPlaytime = 180; // minutes

        when(playtimeService.updatePlaytime(userId, gameId, newPlaytime)).thenReturn(new UserToPlaytime());

        // Act
        ResponseEntity<String> response = playtimeController.updatePlaytime(userId, gameId, newPlaytime);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(playtimeService, times(1)).updatePlaytime(userId, gameId, newPlaytime);
    }

    /**
     * Test the updatePlaytime endpoint when the playtime record does not exist.
     */
    @Test
    public void testUpdatePlaytime_PlaytimeRecordNotFound() {
        // Arrange
        String userId = "user123";
        String gameId = "game456";
        int newPlaytime = 180; // minutes

        doThrow(new IllegalArgumentException("Playtime record does not exist."))
                .when(playtimeService).updatePlaytime(userId, gameId, newPlaytime);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            playtimeController.updatePlaytime(userId, gameId, newPlaytime);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Playtime record does not exist.", exception.getReason());
        verify(playtimeService, times(1)).updatePlaytime(userId, gameId, newPlaytime);
    }

    /**
     * Test the getPlaytime endpoint for a successful retrieval of playtime.
     */
    @Test
    public void testGetPlaytime_Success() {
        // Arrange
        String userId = "user123";
        String gameId = "game456";
        int mockPlaytime = 150; // minutes

        when(playtimeService.getPlaytime(userId, gameId)).thenReturn(mockPlaytime);

        // Act
        ResponseEntity<Integer> response = playtimeController.getPlaytime(userId, gameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPlaytime, response.getBody());
        verify(playtimeService, times(1)).getPlaytime(userId, gameId);
    }

    /**
     * Test the getPlaytime endpoint when the playtime record is not found.
     */
    @Test
    public void testGetPlaytime_PlaytimeRecordNotFound() {
        // Arrange
        String userId = "user123";
        String gameId = "game456";

        when(playtimeService.getPlaytime(userId, gameId))
                .thenThrow(new IllegalArgumentException("Playtime record not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            playtimeController.getPlaytime(userId, gameId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Playtime record not found.", exception.getReason());
        verify(playtimeService, times(1)).getPlaytime(userId, gameId);
    }

    /**
     * Test the getPlaytimeRecords endpoint for a successful retrieval of playtime records.
     */
    @Test
    public void testGetPlaytimeRecords_Success() {
        // Arrange
        String userId = "user123";
        UserToPlaytime record1 = new UserToPlaytime(new User().getId(), "game456", 120);
        UserToPlaytime record2 = new UserToPlaytime(new User().getId(), "game789", 200);
        List<UserToPlaytime> mockPlaytimeRecords = Arrays.asList(record1, record2);

        when(playtimeService.getPlaytimeRecords(userId)).thenReturn(mockPlaytimeRecords);

        // Act
        ResponseEntity<List<UserToPlaytime>> response = playtimeController.getPlaytimeRecords(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPlaytimeRecords, response.getBody());
        verify(playtimeService, times(1)).getPlaytimeRecords(userId);
    }

    /**
     * Test the getPlaytimeRecords endpoint when the user is not found.
     */
    @Test
    public void testGetPlaytimeRecords_UserNotFound() {
        // Arrange
        String userId = "user123";

        when(playtimeService.getPlaytimeRecords(userId))
                .thenThrow(new IllegalArgumentException("User not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            playtimeController.getPlaytimeRecords(userId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User not found.", exception.getReason());
        verify(playtimeService, times(1)).getPlaytimeRecords(userId);
    }

    /**
     * Test the deletePlaytime endpoint for a successful deletion of playtime record.
     */
    @Test
    public void testDeletePlaytime_Success() {
        // Arrange
        String userId = "user123";
        String gameId = "game456";

        doNothing().when(playtimeService).deletePlaytime(userId, gameId);

        // Act
        ResponseEntity<String> response = playtimeController.deletePlaytime(userId, gameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(playtimeService, times(1)).deletePlaytime(userId, gameId);
    }

    /**
     * Test the deletePlaytime endpoint when the playtime record does not exist.
     */
    @Test
    public void testDeletePlaytime_PlaytimeRecordNotFound() {
        // Arrange
        String userId = "user123";
        String gameId = "game456";

        doThrow(new IllegalArgumentException("Playtime record does not exist."))
                .when(playtimeService).deletePlaytime(userId, gameId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            playtimeController.deletePlaytime(userId, gameId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Playtime record does not exist.", exception.getReason());
        verify(playtimeService, times(1)).deletePlaytime(userId, gameId);
    }
}
