package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.domain.GameToTag;
import org.gamed.gamelistdatabaseservice.repository.GameToTagRepository;
import org.gamed.gamelistdatabaseservice.service.GameService;
import org.gamed.gamelistdatabaseservice.service.GameToTagService;
import org.gamed.gamelistdatabaseservice.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameToTagServiceIntegrationTests {

    @Mock
    private GameToTagRepository gameToTagRepository;

    @Mock
    private GameService gameService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private GameToTagService gameToTagService;

    @Test
    void testCreateGameToTagSuccess() {
        String gameId = "gameId123";
        String tagId = "tagId123";

        when(gameToTagRepository.existsByGameIdAndTagId(gameId, tagId)).thenReturn(false);
        when(gameToTagRepository.save(any(GameToTag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GameToTag result = gameToTagService.createGameToTag(gameId, tagId);

        assertNotNull(result);
        assertEquals(gameId, result.getGame());
        assertEquals(tagId, result.getTag());

        verify(gameToTagRepository).existsByGameIdAndTagId(gameId, tagId);
        verify(gameToTagRepository).save(any(GameToTag.class));
    }

    @Test
    void testCreateGameToTagThrowsExceptionWhenGameIdIsNull() {
        String gameId = null;
        String tagId = "tagId123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameToTagService.createGameToTag(gameId, tagId)
        );

        assertEquals("Ids cannot be null.", exception.getMessage());
        verify(gameToTagRepository, never()).existsByGameIdAndTagId(anyString(), anyString());
        verify(gameToTagRepository, never()).save(any(GameToTag.class));
    }

    @Test
    void testCreateGameToTagThrowsExceptionWhenTagIdIsNull() {
        String gameId = "gameId123";
        String tagId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameToTagService.createGameToTag(gameId, tagId)
        );

        assertEquals("Ids cannot be null.", exception.getMessage());
        verify(gameToTagRepository, never()).existsByGameIdAndTagId(anyString(), anyString());
        verify(gameToTagRepository, never()).save(any(GameToTag.class));
    }

    @Test
    void testCreateGameToTagThrowsExceptionWhenRelationAlreadyExists() {
        String gameId = "gameId123";
        String tagId = "tagId123";

        when(gameToTagRepository.existsByGameIdAndTagId(gameId, tagId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameToTagService.createGameToTag(gameId, tagId)
        );

        assertEquals("Relation already exists.", exception.getMessage());
        verify(gameToTagRepository).existsByGameIdAndTagId(gameId, tagId);
        verify(gameToTagRepository, never()).save(any(GameToTag.class));
    }

    @Test
    void testGetGameToTagByIdThrowsExceptionWhenNotFound() {
        String id = "gameToTagId123";

        when(gameToTagRepository.getGameToTagById(id)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameToTagService.getGameToTagById(id)
        );

        assertEquals("Given GameToTag id does not correspond to any GameToTag. ID: " + id, exception.getMessage());
        verify(gameToTagRepository).getGameToTagById(id);
    }

    @Test
    void testGetGameToTagsByGameId() {
        String gameId = "gameId123";
        List<GameToTag> gameToTags = Arrays.asList(
                new GameToTag(gameId, "tagId1"),
                new GameToTag(gameId, "tagId2")
        );

        when(gameToTagRepository.getGameToTagsByGameId(gameId)).thenReturn(gameToTags);

        List<GameToTag> result = gameToTagService.getGameToTagsByGameId(gameId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(gameToTags, result);
        verify(gameToTagRepository).getGameToTagsByGameId(gameId);
    }

    @Test
    void testGetGameToTagsByTagId() {
        String tagId = "tagId123";
        List<GameToTag> gameToTags = Arrays.asList(
                new GameToTag("gameId1", tagId),
                new GameToTag("gameId2", tagId)
        );

        when(gameToTagRepository.getGameToTagsByTagId(tagId)).thenReturn(gameToTags);

        List<GameToTag> result = gameToTagService.getGameToTagsByTagId(tagId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(gameToTags, result);
        verify(gameToTagRepository).getGameToTagsByTagId(tagId);
    }

    @Test
    void testGetGameToTagByGameIdAndTagIdSuccess() {
        String gameId = "gameId123";
        String tagId = "tagId123";
        GameToTag gameToTag = new GameToTag(gameId, tagId);

        when(gameToTagRepository.getGameToTagByGameIdAndTagId(gameId, tagId)).thenReturn(gameToTag);

        GameToTag result = gameToTagService.getGameToTagByGameIdAndTagId(gameId, tagId);

        assertNotNull(result);
        assertEquals(gameToTag, result);
        verify(gameToTagRepository).getGameToTagByGameIdAndTagId(gameId, tagId);
    }

    @Test
    void testGetGameToTagByGameIdAndTagIdThrowsExceptionWhenNotFound() {
        String gameId = "gameId123";
        String tagId = "tagId123";

        when(gameToTagRepository.getGameToTagByGameIdAndTagId(gameId, tagId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameToTagService.getGameToTagByGameIdAndTagId(gameId, tagId)
        );

        assertEquals("No GameToTag found for Game ID: " + gameId + " and Tag ID: " + tagId, exception.getMessage());
        verify(gameToTagRepository).getGameToTagByGameIdAndTagId(gameId, tagId);
    }

    @Test
    void testDeleteGameToTagByIdSuccess() {
        String id = "gameToTagId123";

        when(gameToTagRepository.existsById(id)).thenReturn(true);

        gameToTagService.deleteGameToTagById(id);

        verify(gameToTagRepository).existsById(id);
        verify(gameToTagRepository).deleteGameToTagById(id);
    }

    @Test
    void testDeleteGameToTagByIdThrowsExceptionWhenNotFound() {
        String id = "gameToTagId123";

        when(gameToTagRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gameToTagService.deleteGameToTagById(id)
        );

        assertEquals("Given GameToTag id does not correspond to any GameToTag. ID: " + id, exception.getMessage());
        verify(gameToTagRepository).existsById(id);
        verify(gameToTagRepository, never()).deleteGameToTagById(anyString());
    }

    @Test
    void testExistsByGameIdAndTagId() {
        String gameId = "gameId123";
        String tagId = "tagId123";

        when(gameToTagRepository.existsByGameIdAndTagId(gameId, tagId)).thenReturn(true);

        boolean result = gameToTagService.existsByGameIdAndTagId(gameId, tagId);

        assertTrue(result);
        verify(gameToTagRepository).existsByGameIdAndTagId(gameId, tagId);
    }

    @Test
    void testGetAllGameToTags() {
        List<GameToTag> gameToTags = Arrays.asList(
                new GameToTag("gameId1", "tagId1"),
                new GameToTag("gameId2", "tagId2")
        );

        when(gameToTagRepository.findAll()).thenReturn(gameToTags);

        List<GameToTag> result = gameToTagService.getAllGameToTags();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(gameToTags, result);
        verify(gameToTagRepository).findAll();
    }
}
