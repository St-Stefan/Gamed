package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToLike;
import org.gamed.userdatabaseservice.repository.UserRepository;
import org.gamed.userdatabaseservice.repository.UserToLikeRepository;
import org.gamed.userdatabaseservice.service.UserToLikeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserToLikeServiceIntegrationTests {

    @Mock
    private UserToLikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserToLikeService userToLikeService;

    @Test
    void testCreateLikeSuccess() {
        String userId = "user123";
        String itemId = "item123";
        String type = "post";

        when(likeRepository.findByUserAndItemId(userId, itemId)).thenReturn(Optional.empty());
        when(likeRepository.save(any(UserToLike.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserToLike result = userToLikeService.createLike(userId, itemId, type);

        assertNotNull(result);
        assertEquals(userId, result.getUser());
        assertEquals(itemId, result.getItemId());
        assertEquals(type, result.getType());

        verify(likeRepository).findByUserAndItemId(userId, itemId);
        verify(likeRepository).save(any(UserToLike.class));
    }

    @Test
    void testCreateLikeThrowsExceptionWhenUserIdIsNull() {
        String userId = null;
        String itemId = "item123";
        String type = "post";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.createLike(userId, itemId, type)
        );

        assertEquals("User ID, Item ID cannot be null and type cannot be null.", exception.getMessage());

        verify(likeRepository, never()).findByUserAndItemId(anyString(), anyString());
        verify(likeRepository, never()).save(any(UserToLike.class));
    }

    @Test
    void testCreateLikeThrowsExceptionWhenItemIdIsNull() {
        String userId = "user123";
        String itemId = null;
        String type = "post";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.createLike(userId, itemId, type)
        );

        assertEquals("User ID, Item ID cannot be null and type cannot be null.", exception.getMessage());

        verify(likeRepository, never()).findByUserAndItemId(anyString(), anyString());
        verify(likeRepository, never()).save(any(UserToLike.class));
    }

    @Test
    void testCreateLikeThrowsExceptionWhenTypeIsNull() {
        String userId = "user123";
        String itemId = "item123";
        String type = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.createLike(userId, itemId, type)
        );

        assertEquals("User ID, Item ID cannot be null and type cannot be null.", exception.getMessage());

        verify(likeRepository, never()).findByUserAndItemId(anyString(), anyString());
        verify(likeRepository, never()).save(any(UserToLike.class));
    }

    @Test
    void testCreateLikeThrowsExceptionWhenRelationAlreadyExists() {
        String userId = "user123";
        String itemId = "item123";
        String type = "post";

        when(likeRepository.findByUserAndItemId(userId, itemId)).thenReturn(Optional.of(new UserToLike(userId, itemId, type)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.createLike(userId, itemId, type)
        );

        assertEquals("Relation already exists.", exception.getMessage());

        verify(likeRepository).findByUserAndItemId(userId, itemId);
        verify(likeRepository, never()).save(any(UserToLike.class));
    }

    @Test
    void testGetLikeThrowsExceptionWhenNotFound() {
        String id = "like123";

        when(likeRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.getLike(id)
        );

        assertEquals("Given UserToLike id does not correspond to any UserToLike. ID: " + id, exception.getMessage());

        verify(likeRepository).findById(id);
    }

    @Test
    void testGetLikedItemsSuccess() {
        String userId = "user123";
        List<UserToLike> likes = Arrays.asList(
                new UserToLike(userId, "item1", "post"),
                new UserToLike(userId, "item2", "comment")
        );

        when(userRepository.existsById(userId)).thenReturn(true);
        when(likeRepository.findByUserId(userId)).thenReturn(likes);

        List<UserToLike> result = userToLikeService.getLikedItems(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(likes, result);

        verify(userRepository).existsById(userId);
        verify(likeRepository).findByUserId(userId);
    }

    @Test
    void testGetLikedItemsThrowsExceptionWhenUserNotFound() {
        String userId = "user123";

        when(userRepository.existsById(userId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.getLikedItems(userId)
        );

        assertEquals("User with ID user123 does not exist.", exception.getMessage());

        verify(userRepository).existsById(userId);
        verify(likeRepository, never()).findByUserId(anyString());
    }

    @Test
    void testDeleteLikeByIdSuccess() {
        String id = "like123";

        when(likeRepository.existsById(id)).thenReturn(true);

        userToLikeService.deleteLike(id);

        verify(likeRepository).existsById(id);
        verify(likeRepository).deleteById(id);
    }

    @Test
    void testDeleteLikeByIdThrowsExceptionWhenNotFound() {
        String id = "like123";

        when(likeRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.deleteLike(id)
        );

        assertEquals("Given UserToLike id does not correspond to any UserToLike. ID: " + id, exception.getMessage());

        verify(likeRepository).existsById(id);
        verify(likeRepository, never()).deleteById(anyString());
    }

    @Test
    void testDeleteLikeByUserAndItemSuccess() {
        String userId = "user123";
        String itemId = "item123";
        UserToLike like = new UserToLike(userId, itemId, "post");

        when(likeRepository.findByUserAndItemId(userId, itemId)).thenReturn(Optional.of(like));

        userToLikeService.deleteLike(userId, itemId);

        verify(likeRepository).findByUserAndItemId(userId, itemId);
        verify(likeRepository).delete(like);
    }

    @Test
    void testDeleteLikeByUserAndItemThrowsExceptionWhenNotFound() {
        String userId = "user123";
        String itemId = "item123";

        when(likeRepository.findByUserAndItemId(userId, itemId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.deleteLike(userId, itemId)
        );

        assertEquals("No like record found for the given user and item", exception.getMessage());

        verify(likeRepository).findByUserAndItemId(userId, itemId);
        verify(likeRepository, never()).delete(any(UserToLike.class));
    }

    @Test
    void testIsLikingItemReturnsTrueWhenLiking() {
        String userId = "user123";
        String itemId = "item123";

        when(likeRepository.findByUserAndItemId(userId, itemId)).thenReturn(Optional.of(new UserToLike()));

        boolean result = userToLikeService.isLikingItem(userId, itemId);

        assertTrue(result);

        verify(likeRepository).findByUserAndItemId(userId, itemId);
    }

    @Test
    void testIsLikingItemReturnsFalseWhenNotLiking() {
        String userId = "user123";
        String itemId = "item123";

        when(likeRepository.findByUserAndItemId(userId, itemId)).thenReturn(Optional.empty());

        boolean result = userToLikeService.isLikingItem(userId, itemId);

        assertFalse(result);

        verify(likeRepository).findByUserAndItemId(userId, itemId);
    }

    @Test
    void testIsLikingItemThrowsExceptionWhenUserIdIsNull() {
        String userId = null;
        String itemId = "item123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.isLikingItem(userId, itemId)
        );

        assertEquals("User ID and Item ID cannot be null.", exception.getMessage());

        verify(likeRepository, never()).findByUserAndItemId(anyString(), anyString());
    }

    @Test
    void testIsLikingItemThrowsExceptionWhenItemIdIsNull() {
        String userId = "user123";
        String itemId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToLikeService.isLikingItem(userId, itemId)
        );

        assertEquals("User ID and Item ID cannot be null.", exception.getMessage());

        verify(likeRepository, never()).findByUserAndItemId(anyString(), anyString());
    }

    @Test
    void testGetLikersSuccess() {
        String itemId = "item123";
        List<UserToLike> likes = Arrays.asList(
                new UserToLike("user1", itemId, "post"),
                new UserToLike("user2", itemId, "post")
        );

        when(likeRepository.findByItemId(itemId)).thenReturn(likes);

        List<String> result = userToLikeService.getLikers(itemId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("user1"));
        assertTrue(result.contains("user2"));

        verify(likeRepository).findByItemId(itemId);
    }

    @Test
    void testGetLikersReturnsEmptyListWhenNoLikers() {
        String itemId = "item123";

        when(likeRepository.findByItemId(itemId)).thenReturn(Collections.emptyList());

        List<String> result = userToLikeService.getLikers(itemId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(likeRepository).findByItemId(itemId);
    }
}
