package org.gamed.gamelistdatabaseservice.integration;

import org.gamed.gamelistdatabaseservice.domain.ListToTag;
import org.gamed.gamelistdatabaseservice.repository.ListToTagRepository;
import org.gamed.gamelistdatabaseservice.service.GameListService;
import org.gamed.gamelistdatabaseservice.service.ListToTagService;
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
public class ListToTagServiceIntegrationTests {

    @Mock
    private ListToTagRepository listToTagRepository;

    @Mock
    private GameListService gameListService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private ListToTagService listToTagService;

    @Test
    void testCreateListToTagSuccess() {
        String listId = "listId123";
        String tagId = "tagId123";

        when(listToTagRepository.existsByListIdAndTagId(listId, tagId)).thenReturn(false);
        when(listToTagRepository.save(any(ListToTag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ListToTag result = listToTagService.createListToTag(listId, tagId);

        assertNotNull(result);
        assertEquals(listId, result.getList());
        assertEquals(tagId, result.getTag());

        verify(listToTagRepository).existsByListIdAndTagId(listId, tagId);
        verify(listToTagRepository).save(any(ListToTag.class));
    }

    @Test
    void testCreateListToTagThrowsExceptionWhenListIdIsNull() {
        String listId = null;
        String tagId = "tagId123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToTagService.createListToTag(listId, tagId)
        );

        assertEquals("Ids cannot be null", exception.getMessage());

        verify(listToTagRepository, never()).existsByListIdAndTagId(anyString(), anyString());
        verify(listToTagRepository, never()).save(any(ListToTag.class));
    }

    @Test
    void testCreateListToTagThrowsExceptionWhenTagIdIsNull() {
        String listId = "listId123";
        String tagId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToTagService.createListToTag(listId, tagId)
        );

        assertEquals("Ids cannot be null", exception.getMessage());

        verify(listToTagRepository, never()).existsByListIdAndTagId(anyString(), anyString());
        verify(listToTagRepository, never()).save(any(ListToTag.class));
    }

    @Test
    void testCreateListToTagThrowsExceptionWhenRelationAlreadyExists() {
        String listId = "listId123";
        String tagId = "tagId123";

        when(listToTagRepository.existsByListIdAndTagId(listId, tagId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToTagService.createListToTag(listId, tagId)
        );

        assertEquals("Relation already exists.", exception.getMessage());

        verify(listToTagRepository).existsByListIdAndTagId(listId, tagId);
        verify(listToTagRepository, never()).save(any(ListToTag.class));
    }

    @Test
    void testGetListToTagByIdThrowsExceptionWhenNotFound() {
        String id = "listToTagId123";

        when(listToTagRepository.getListToTagById(id)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToTagService.getListToTagById(id)
        );

        assertEquals("Given ListToTag id does not correspond to any ListToTag. ID: " + id, exception.getMessage());

        verify(listToTagRepository).getListToTagById(id);
    }

    @Test
    void testGetListToTagsByListId() {
        String listId = "listId123";
        List<ListToTag> listToTags = Arrays.asList(
                new ListToTag(listId, "tagId1"),
                new ListToTag(listId, "tagId2")
        );

        when(listToTagRepository.getListToTagsByListId(listId)).thenReturn(listToTags);

        List<ListToTag> result = listToTagService.getListToTagsByListId(listId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(listToTags, result);

        verify(listToTagRepository).getListToTagsByListId(listId);
    }

    @Test
    void testGetListToTagsByTagId() {
        String tagId = "tagId123";
        List<ListToTag> listToTags = Arrays.asList(
                new ListToTag("listId1", tagId),
                new ListToTag("listId2", tagId)
        );

        when(listToTagRepository.getListToTagsByTagId(tagId)).thenReturn(listToTags);

        List<ListToTag> result = listToTagService.getListToTagsByTagId(tagId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(listToTags, result);

        verify(listToTagRepository).getListToTagsByTagId(tagId);
    }

    @Test
    void testGetListToTagByListIdAndTagIdSuccess() {
        String listId = "listId123";
        String tagId = "tagId123";
        ListToTag listToTag = new ListToTag(listId, tagId);

        when(listToTagRepository.getListToTagByListIdAndTagId(listId, tagId)).thenReturn(listToTag);

        ListToTag result = listToTagService.getListToTagByListIdAndTagId(listId, tagId);

        assertNotNull(result);
        assertEquals(listToTag, result);

        verify(listToTagRepository).getListToTagByListIdAndTagId(listId, tagId);
    }

    @Test
    void testGetListToTagByListIdAndTagIdThrowsExceptionWhenNotFound() {
        String listId = "listId123";
        String tagId = "tagId123";

        when(listToTagRepository.getListToTagByListIdAndTagId(listId, tagId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToTagService.getListToTagByListIdAndTagId(listId, tagId)
        );

        assertEquals("No ListToTag found for List ID: " + listId + " and Tag ID: " + tagId, exception.getMessage());

        verify(listToTagRepository).getListToTagByListIdAndTagId(listId, tagId);
    }

    @Test
    void testDeleteListToTagByIdSuccess() {
        String id = "listToTagId123";

        when(listToTagRepository.existsById(id)).thenReturn(true);

        listToTagService.deleteListToTagById(id);

        verify(listToTagRepository).existsById(id);
        verify(listToTagRepository).deleteListToTagById(id);
    }

    @Test
    void testDeleteListToTagByIdThrowsExceptionWhenNotFound() {
        String id = "listToTagId123";

        when(listToTagRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listToTagService.deleteListToTagById(id)
        );

        assertEquals("Given ListToTag id does not correspond to any ListToTag. ID: " + id, exception.getMessage());

        verify(listToTagRepository).existsById(id);
        verify(listToTagRepository, never()).deleteListToTagById(anyString());
    }

    @Test
    void testExistsByListIdAndTagId() {
        String listId = "listId123";
        String tagId = "tagId123";

        when(listToTagRepository.existsByListIdAndTagId(listId, tagId)).thenReturn(true);

        boolean result = listToTagService.existsByListIdAndTagId(listId, tagId);

        assertTrue(result);

        verify(listToTagRepository).existsByListIdAndTagId(listId, tagId);
    }

    @Test
    void testGetAllListToTags() {
        List<ListToTag> listToTags = Arrays.asList(
                new ListToTag("listId1", "tagId1"),
                new ListToTag("listId2", "tagId2")
        );

        when(listToTagRepository.findAll()).thenReturn(listToTags);

        List<ListToTag> result = listToTagService.getAllListToTags();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(listToTags, result);

        verify(listToTagRepository).findAll();
    }
}
