package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.controllers.UserToLikeController;
import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToLike;
import org.gamed.userdatabaseservice.service.UserToLikeService;
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
import java.util.stream.Collectors;
import java.util.AbstractMap.SimpleEntry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for UserToLikeController.
 */
public class UserToLikeControllerIntegrationTests {

    @Mock
    private UserToLikeService likeService;

    @InjectMocks
    private UserToLikeController likeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the likeItem endpoint for a successful like operation.
     */
    @Test
    public void testLikeItem_Success() {
        // Arrange
        String userId = "user123";
        String itemId = "item456";
        String type = "post";

        when(likeService.createLike(userId, itemId, type)).thenReturn(new UserToLike());

        // Act
        ResponseEntity<String> response = likeController.likeItem(userId, itemId, type);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(likeService, times(1)).createLike(userId, itemId, type);
    }

    /**
     * Test the likeItem endpoint when an exception occurs.
     */
    @Test
    public void testLikeItem_Failure() {
        // Arrange
        String userId = "user123";
        String itemId = "item456";
        String type = "post";

        doThrow(new IllegalArgumentException("Item already liked."))
                .when(likeService).createLike(userId, itemId, type);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            likeController.likeItem(userId, itemId, type);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Item already liked.", exception.getReason());
        verify(likeService, times(1)).createLike(userId, itemId, type);
    }

    /**
     * Test the unlikeItem endpoint for a successful unlike operation.
     */
    @Test
    public void testUnlikeItem_Success() {
        // Arrange
        String userId = "user123";
        String itemId = "item456";

        doNothing().when(likeService).deleteLike(userId, itemId);

        // Act
        ResponseEntity<String> response = likeController.unlikeItem(userId, itemId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(likeService, times(1)).deleteLike(userId, itemId);
    }

    /**
     * Test the unlikeItem endpoint when the like is not found.
     */
    @Test
    public void testUnlikeItem_LikeNotFound() {
        // Arrange
        String userId = "user123";
        String itemId = "item456";

        doThrow(new IllegalArgumentException("Like relationship does not exist."))
                .when(likeService).deleteLike(userId, itemId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            likeController.unlikeItem(userId, itemId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Like relationship does not exist.", exception.getReason());
        verify(likeService, times(1)).deleteLike(userId, itemId);
    }

    /**
     * Test the getLikedItems endpoint for a successful retrieval of liked items.
     */
    @Test
    public void testGetLikedItems_Success() {
        // Arrange
        String userId = "user123";
        UserToLike like1 = new UserToLike(new User().getId(), "item456", "post");
        UserToLike like2 = new UserToLike(new User().getId(), "item789", "comment");
        List<UserToLike> mockLikedItems = Arrays.asList(like1, like2);

        when(likeService.getLikedItems(userId)).thenReturn(mockLikedItems);

        // Act
        ResponseEntity<List<SimpleEntry<String, String>>> response = likeController.getLikedItems(userId);
        List<String> itemIds = response.getBody().stream().map(SimpleEntry::getKey).toList();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<String> expectedItemIds = mockLikedItems.stream()
                .map(UserToLike::getItemId)
                .collect(Collectors.toList());
        assertEquals(expectedItemIds, itemIds);
        verify(likeService, times(1)).getLikedItems(userId);
    }

    /**
     * Test the getLikedItems endpoint when an exception occurs.
     */
    @Test
    public void testGetLikedItems_Failure() {
        // Arrange
        String userId = "user123";

        when(likeService.getLikedItems(userId))
                .thenThrow(new IllegalArgumentException("User not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            likeController.getLikedItems(userId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User not found.", exception.getReason());
        verify(likeService, times(1)).getLikedItems(userId);
    }

    /**
     * Test the getLikers endpoint for a successful retrieval of likers.
     */
    @Test
    public void testGetLikers_Success() {
        // Arrange
        String itemId = "item456";
        User liker1 = new User("UserA", "usera@example.com", "hashed_pw1", true, false);
        User liker2 = new User("UserB", "userb@example.com", "hashed_pw2", false, true);
        List<String> mockLikers = Arrays.asList(liker1.getId(), liker2.getId());

        when(likeService.getLikers(itemId)).thenReturn(mockLikers);

        // Act
        ResponseEntity<List<String>> response = likeController.getLikers(itemId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockLikers, response.getBody());
        verify(likeService, times(1)).getLikers(itemId);
    }

    /**
     * Test the getLikers endpoint when an exception occurs.
     */
    @Test
    public void testGetLikers_Failure() {
        // Arrange
        String itemId = "item456";

        when(likeService.getLikers(itemId))
                .thenThrow(new IllegalArgumentException("Item not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            likeController.getLikers(itemId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Item not found.", exception.getReason());
        verify(likeService, times(1)).getLikers(itemId);
    }

    /**
     * Test the isLikingItem endpoint when the user is liking the item.
     */
    @Test
    public void testIsLikingItem_Liking() {
        // Arrange
        String userId = "user123";
        String itemId = "item456";

        when(likeService.isLikingItem(userId, itemId)).thenReturn(true);

        // Act
        ResponseEntity<String> response = likeController.isLikingItem(userId, itemId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(likeService, times(1)).isLikingItem(userId, itemId);
    }

    /**
     * Test the isLikingItem endpoint when the user is not liking the item.
     */
    @Test
    public void testIsLikingItem_NotLiking() {
        // Arrange
        String userId = "user123";
        String itemId = "item456";

        when(likeService.isLikingItem(userId, itemId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = likeController.isLikingItem(userId, itemId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(likeService, times(1)).isLikingItem(userId, itemId);
    }

    /**
     * Test the isLikingItem endpoint when an exception occurs.
     */
    @Test
    public void testIsLikingItem_Failure() {
        // Arrange
        String userId = "user123";
        String itemId = "item456";

        when(likeService.isLikingItem(userId, itemId))
                .thenThrow(new IllegalArgumentException("Invalid user or item ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            likeController.isLikingItem(userId, itemId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid user or item ID.", exception.getReason());
        verify(likeService, times(1)).isLikingItem(userId, itemId);
    }
}
