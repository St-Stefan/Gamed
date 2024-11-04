package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.domain.UserToPlaytime;
import org.gamed.userdatabaseservice.repository.UserToPlaytimeRepository;
import org.gamed.userdatabaseservice.service.UserToPlaytimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserToPlaytimeServiceIntegrationTests {

    @Mock
    private UserToPlaytimeRepository playtimeRepository;

    @InjectMocks
    private UserToPlaytimeService userToPlaytimeService;

    @Test
    void testCreatePlaytimeSuccess() {
        String userId = "user123";
        String gameId = "game123";
        int playtime = 5;

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.empty());
        when(playtimeRepository.save(any(UserToPlaytime.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserToPlaytime result = userToPlaytimeService.createPlaytime(userId, gameId, playtime);

        assertNotNull(result);
        assertEquals(userId, result.getUser());
        assertEquals(gameId, result.getGameId());
        assertEquals(playtime, result.getPlaytime());

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
        verify(playtimeRepository).save(any(UserToPlaytime.class));
    }

    @Test
    void testCreatePlaytimeThrowsExceptionWhenUserIdIsNull() {
        String userId = null;
        String gameId = "game123";
        int playtime = 5;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.createPlaytime(userId, gameId, playtime)
        );

        assertEquals("User ID, Game ID cannot be null and playtime cannot be negative.", exception.getMessage());

        verify(playtimeRepository, never()).findByUserIdAndGameId(anyString(), anyString());
        verify(playtimeRepository, never()).save(any(UserToPlaytime.class));
    }

    @Test
    void testCreatePlaytimeThrowsExceptionWhenGameIdIsNull() {
        String userId = "user123";
        String gameId = null;
        int playtime = 5;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.createPlaytime(userId, gameId, playtime)
        );

        assertEquals("User ID, Game ID cannot be null and playtime cannot be negative.", exception.getMessage());

        verify(playtimeRepository, never()).findByUserIdAndGameId(anyString(), anyString());
        verify(playtimeRepository, never()).save(any(UserToPlaytime.class));
    }

    @Test
    void testCreatePlaytimeThrowsExceptionWhenPlaytimeIsNegative() {
        String userId = "user123";
        String gameId = "game123";
        int playtime = -5;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.createPlaytime(userId, gameId, playtime)
        );

        assertEquals("User ID, Game ID cannot be null and playtime cannot be negative.", exception.getMessage());

        verify(playtimeRepository, never()).findByUserIdAndGameId(anyString(), anyString());
        verify(playtimeRepository, never()).save(any(UserToPlaytime.class));
    }

    @Test
    void testCreatePlaytimeThrowsExceptionWhenRelationAlreadyExists() {
        String userId = "user123";
        String gameId = "game123";
        int playtime = 5;

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId))
                .thenReturn(Optional.of(new UserToPlaytime(userId, gameId, playtime)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.createPlaytime(userId, gameId, playtime)
        );

        assertEquals("Relation already exists.", exception.getMessage());

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
        verify(playtimeRepository, never()).save(any(UserToPlaytime.class));
    }

    @Test
    void testUpdatePlaytimeSuccess() {
        String userId = "user123";
        String gameId = "game123";
        int newPlaytime = 10;

        UserToPlaytime existingPlaytime = new UserToPlaytime(userId, gameId, 5);

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.of(existingPlaytime));
        when(playtimeRepository.save(existingPlaytime)).thenReturn(existingPlaytime);

        UserToPlaytime result = userToPlaytimeService.updatePlaytime(userId, gameId, newPlaytime);

        assertNotNull(result);
        assertEquals(userId, result.getUser());
        assertEquals(gameId, result.getGameId());
        assertEquals(newPlaytime, result.getPlaytime());

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
        verify(playtimeRepository).save(existingPlaytime);
    }

    @Test
    void testUpdatePlaytimeThrowsExceptionWhenEntryDoesNotExist() {
        String userId = "user123";
        String gameId = "game123";
        int newPlaytime = 10;

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.updatePlaytime(userId, gameId, newPlaytime)
        );

        assertEquals("Playtime entry for the given user and game does not exist.", exception.getMessage());

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
        verify(playtimeRepository, never()).save(any(UserToPlaytime.class));
    }

    @Test
    void testUpdatePlaytimeThrowsExceptionWhenPlaytimeIsNegative() {
        String userId = "user123";
        String gameId = "game123";
        int newPlaytime = -10;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.updatePlaytime(userId, gameId, newPlaytime)
        );

        assertEquals("User ID, Game ID cannot be null and playtime cannot be negative.", exception.getMessage());

        verify(playtimeRepository, never()).findByUserIdAndGameId(anyString(), anyString());
        verify(playtimeRepository, never()).save(any(UserToPlaytime.class));
    }

    @Test
    void testGetPlaytimeByIdThrowsExceptionWhenNotFound() {
        String id = "playtime123";

        when(playtimeRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.getPlaytime(id)
        );

        assertEquals("Given UserToPlaytime id does not correspond to any UserToPlaytime. ID: " + id, exception.getMessage());

        verify(playtimeRepository).findById(id);
    }

    @Test
    void testGetPlaytimeRecordsSuccess() {
        String userId = "user123";
        List<UserToPlaytime> playtimeRecords = Arrays.asList(
                new UserToPlaytime(userId, "game1", 5),
                new UserToPlaytime(userId, "game2", 10)
        );

        when(playtimeRepository.findByUserId(userId)).thenReturn(playtimeRecords);

        List<UserToPlaytime> result = userToPlaytimeService.getPlaytimeRecords(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(playtimeRecords, result);

        verify(playtimeRepository).findByUserId(userId);
    }

    @Test
    void testGetPlaytimeSuccess() {
        String userId = "user123";
        String gameId = "game123";
        int playtimeValue = 15;

        UserToPlaytime playtimeEntry = new UserToPlaytime(userId, gameId, playtimeValue);

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.of(playtimeEntry));

        int result = userToPlaytimeService.getPlaytime(userId, gameId);

        assertEquals(playtimeValue, result);

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
    }

    @Test
    void testGetPlaytimeThrowsExceptionWhenNotFound() {
        String userId = "user123";
        String gameId = "game123";

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.getPlaytime(userId, gameId)
        );

        assertEquals("No playtime found for the given user and game", exception.getMessage());

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
    }

    @Test
    void testDeletePlaytimeSuccess() {
        String userId = "user123";
        String gameId = "game123";
        UserToPlaytime playtimeEntry = new UserToPlaytime(userId, gameId, 5);

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.of(playtimeEntry));

        userToPlaytimeService.deletePlaytime(userId, gameId);

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
        verify(playtimeRepository).delete(playtimeEntry);
    }

    @Test
    void testDeletePlaytimeThrowsExceptionWhenNotFound() {
        String userId = "user123";
        String gameId = "game123";

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.deletePlaytime(userId, gameId)
        );

        assertEquals("No playtime record found for the given user and game", exception.getMessage());

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
        verify(playtimeRepository, never()).delete(any(UserToPlaytime.class));
    }

    @Test
    void testPlaytimeExistsReturnsTrueWhenExists() {
        String userId = "user123";
        String gameId = "game123";

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.of(new UserToPlaytime()));

        boolean result = userToPlaytimeService.playtimeExists(userId, gameId);

        assertTrue(result);

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
    }

    @Test
    void testPlaytimeExistsReturnsFalseWhenNotExists() {
        String userId = "user123";
        String gameId = "game123";

        when(playtimeRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.empty());

        boolean result = userToPlaytimeService.playtimeExists(userId, gameId);

        assertFalse(result);

        verify(playtimeRepository).findByUserIdAndGameId(userId, gameId);
    }

    @Test
    void testPlaytimeExistsThrowsExceptionWhenUserIdIsNull() {
        String userId = null;
        String gameId = "game123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.playtimeExists(userId, gameId)
        );

        assertEquals("User ID and Game ID cannot be null.", exception.getMessage());

        verify(playtimeRepository, never()).findByUserIdAndGameId(anyString(), anyString());
    }

    @Test
    void testPlaytimeExistsThrowsExceptionWhenGameIdIsNull() {
        String userId = "user123";
        String gameId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToPlaytimeService.playtimeExists(userId, gameId)
        );

        assertEquals("User ID and Game ID cannot be null.", exception.getMessage());

        verify(playtimeRepository, never()).findByUserIdAndGameId(anyString(), anyString());
    }
}
