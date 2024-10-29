package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.domain.UserToFollowedList;
import org.gamed.userdatabaseservice.repository.UserToFollowedListRepository;
import org.gamed.userdatabaseservice.service.UserToFollowedListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserToFollowedListServiceIntegrationTests {

    @Mock
    private UserToFollowedListRepository userToFollowedListRepository;

    @InjectMocks
    private UserToFollowedListService userToFollowedListService;

    @Test
    void testCreateFollowedListSuccess() {
        String userId = "user123";
        String listId = "list123";

        when(userToFollowedListRepository.findByUserAndListId(userId, listId)).thenReturn(Optional.empty());
        when(userToFollowedListRepository.save(any(UserToFollowedList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserToFollowedList result = userToFollowedListService.createFollowedList(userId, listId);

        assertNotNull(result);
        assertEquals(userId, result.getUser());
        assertEquals(listId, result.getListId());

        verify(userToFollowedListRepository).findByUserAndListId(userId, listId);
        verify(userToFollowedListRepository).save(any(UserToFollowedList.class));
    }

    @Test
    void testCreateFollowedListThrowsExceptionWhenUserIsNull() {
        String userId = null;
        String listId = "list123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedListService.createFollowedList(userId, listId)
        );

        assertEquals("Ids cannot be null.", exception.getMessage());

        verify(userToFollowedListRepository, never()).findByUserAndListId(anyString(), anyString());
        verify(userToFollowedListRepository, never()).save(any(UserToFollowedList.class));
    }

    @Test
    void testCreateFollowedListThrowsExceptionWhenFollowedListIdIsNull() {
        String userId = "user123";
        String listId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedListService.createFollowedList(userId, listId)
        );

        assertEquals("Ids cannot be null.", exception.getMessage());

        verify(userToFollowedListRepository, never()).findByUserAndListId(anyString(), anyString());
        verify(userToFollowedListRepository, never()).save(any(UserToFollowedList.class));
    }

    @Test
    void testCreateFollowedListThrowsExceptionWhenRelationAlreadyExists() {
        String userId = "user123";
        String listId = "list123";

        when(userToFollowedListRepository.findByUserAndListId(userId, listId)).thenReturn(Optional.of(new UserToFollowedList()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedListService.createFollowedList(userId, listId)
        );

        assertEquals("Relation already exists.", exception.getMessage());

        verify(userToFollowedListRepository).findByUserAndListId(userId, listId);
        verify(userToFollowedListRepository, never()).save(any(UserToFollowedList.class));
    }

    @Test
    void testGetFollowedListSuccess() {
        String id = "followedListId123";
        UserToFollowedList followedList = new UserToFollowedList("user123", "list123");

        when(userToFollowedListRepository.findById(id)).thenReturn(Optional.of(followedList));

        UserToFollowedList result = userToFollowedListService.getFollowedList(id);

        assertNotNull(result);
        assertEquals(followedList, result);

        verify(userToFollowedListRepository).findById(id);
    }

    @Test
    void testGetFollowedListThrowsExceptionWhenNotFound() {
        String id = "followedListId123";

        when(userToFollowedListRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedListService.getFollowedList(id)
        );

        assertEquals("Given UserToFollowedList id does not correspond to any UserToFollowedList. ID: " + id, exception.getMessage());

        verify(userToFollowedListRepository).findById(id);
    }

    @Test
    void testDeleteFollowedListSuccess() {
        String id = "followedListId123";

        when(userToFollowedListRepository.existsById(id)).thenReturn(true);

        userToFollowedListService.deleteFollowedList(id);

        verify(userToFollowedListRepository).existsById(id);
        verify(userToFollowedListRepository).deleteById(id);
    }

    @Test
    void testDeleteFollowedListThrowsExceptionWhenNotFound() {
        String id = "followedListId123";

        when(userToFollowedListRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedListService.deleteFollowedList(id)
        );

        assertEquals("Given UserToFollowedList id does not correspond to any UserToFollowedList. ID: " + id, exception.getMessage());

        verify(userToFollowedListRepository).existsById(id);
        verify(userToFollowedListRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetUserToFollowedListSuccess() {
        String userId = "user123";
        String listId = "list123";
        UserToFollowedList followedList = new UserToFollowedList(userId, listId);

        when(userToFollowedListRepository.findByUserAndListId(userId, listId)).thenReturn(Optional.of(followedList));

        UserToFollowedList result = userToFollowedListService.getUserToFollowedList(userId, listId);

        assertNotNull(result);
        assertEquals(followedList, result);

        verify(userToFollowedListRepository).findByUserAndListId(userId, listId);
    }

    @Test
    void testGetUserToFollowedListThrowsExceptionWhenNotFound() {
        String userId = "user123";
        String listId = "list123";

        when(userToFollowedListRepository.findByUserAndListId(userId, listId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedListService.getUserToFollowedList(userId, listId)
        );

        assertEquals("No UserToFollowedList found for user ID: " + userId + " and list ID: " + listId, exception.getMessage());

        verify(userToFollowedListRepository).findByUserAndListId(userId, listId);
    }

    @Test
    void testGetFollowedListIdSuccess() {
        String userId = "user123";
        String listId = "list123";
        String id = "followedListId123";
        UserToFollowedList followedList = new UserToFollowedList(userId, listId);

        when(userToFollowedListRepository.findByUserAndListId(userId, listId)).thenReturn(Optional.of(followedList));

        String result = userToFollowedListService.getFollowedListId(userId, listId);

        assertNull(result);

        verify(userToFollowedListRepository).findByUserAndListId(userId, listId);
    }

    @Test
    void testGetFollowedListIdThrowsExceptionWhenNotFound() {
        String userId = "user123";
        String listId = "list123";

        when(userToFollowedListRepository.findByUserAndListId(userId, listId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedListService.getFollowedListId(userId, listId)
        );

        assertEquals("Followed list entry for the given user and list does not exist.", exception.getMessage());

        verify(userToFollowedListRepository).findByUserAndListId(userId, listId);
    }

    @Test
    void testGetFollowers() {
        String listId = "list123";
        List<UserToFollowedList> followedListEntries = Arrays.asList(
                new UserToFollowedList("user1", listId),
                new UserToFollowedList("user2", listId)
        );

        when(userToFollowedListRepository.findByListId(listId)).thenReturn(followedListEntries);

        List<String> result = userToFollowedListService.getFollowers(listId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("user1"));
        assertTrue(result.contains("user2"));

        verify(userToFollowedListRepository).findByListId(listId);
    }

    @Test
    void testGetFollowedLists() {
        String userId = "user123";
        List<UserToFollowedList> followedLists = Arrays.asList(
                new UserToFollowedList(userId, "list1"),
                new UserToFollowedList(userId, "list2")
        );

        when(userToFollowedListRepository.findByUserId(userId)).thenReturn(followedLists);

        List<String> result = userToFollowedListService.getFollowedLists(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("list1"));
        assertTrue(result.contains("list2"));

        verify(userToFollowedListRepository).findByUserId(userId);
    }

    @Test
    void testIsFollowingListReturnsTrueWhenFollowing() {
        String userId = "user123";
        String listId = "list123";
        UserToFollowedList followedList = new UserToFollowedList(userId, listId);

        when(userToFollowedListRepository.findByUserAndListId(userId, listId)).thenReturn(Optional.of(followedList));

        boolean result = userToFollowedListService.isFollowingList(userId, listId);

        assertTrue(result);

        verify(userToFollowedListRepository).findByUserAndListId(userId, listId);
    }

    @Test
    void testIsFollowingListReturnsFalseWhenNotFollowing() {
        String userId = "user123";
        String listId = "list123";

        when(userToFollowedListRepository.findByUserAndListId(userId, listId)).thenReturn(Optional.empty());

        boolean result = userToFollowedListService.isFollowingList(userId, listId);

        assertFalse(result);

        verify(userToFollowedListRepository).findByUserAndListId(userId, listId);
    }
}
