package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.controllers.UserToFollowedListController;
import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.service.UserService;
import org.gamed.userdatabaseservice.service.UserToFollowedListService;
import org.gamed.userdatabaseservice.domain.UserToFollowedList;
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
 * Test class for UserToFollowedListController.
 */
public class UserToFollowedListControllerIntegrationTests {

    @Mock
    private UserToFollowedListService followedListService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserToFollowedListController followedListController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the followList endpoint for a successful follow operation.
     */
    @Test
    public void testFollowList_Success() {
        // Arrange
        String userId = "user123";
        String listId = "list456";
        User mockUser = new User("User123", "user123@example.com", "hashed_password", true, false);

        when(userService.getUser(userId)).thenReturn(mockUser);
        when(followedListService.createFollowedList(any(), anyString())).thenReturn(new UserToFollowedList());

        // Act
        ResponseEntity<String> response = followedListController.followList(userId, listId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(followedListService, times(1)).createFollowedList(any(), anyString());
    }

    /**
     * Test the followList endpoint when an exception occurs.
     */
    @Test
    public void testFollowList_Failure() {
        // Arrange
        String userId = null;
        String listId = "list456";
        User mockUser = new User("User123", "user123@example.com", "hashed_password", true, false);

        when(userService.getUser(userId)).thenReturn(mockUser);
        doThrow(new IllegalArgumentException("List already followed."))
                .when(followedListService).createFollowedList(mockUser.getId(), listId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedListController.followList(userId, listId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("List already followed.", exception.getReason());
        verify(followedListService, times(1)).createFollowedList(mockUser.getId(), listId);
    }

    /**
     * Test the unfollowList endpoint for a successful unfollow operation.
     */
    @Test
    public void testUnfollowList_Success() {
        // Arrange
        String userId = "user123";
        String listId = "list456";
        String followedListId = "followedList789";
        UserToFollowedList mockFollowedList = new UserToFollowedList(new User().getId(), listId);

        when(followedListService.getUserToFollowedList(userId, listId)).thenReturn(mockFollowedList);
        doNothing().when(followedListService).deleteFollowedList(followedListId);

        // Act
        ResponseEntity<String> response = followedListController.unfollowList(userId, listId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(followedListService, times(1)).getUserToFollowedList(userId, listId);
        verify(followedListService, times(1)).deleteFollowedList(null);
    }

    /**
     * Test the unfollowList endpoint when the followed list is not found.
     */
    @Test
    public void testUnfollowList_FollowedListNotFound() {
        // Arrange
        String userId = "user123";
        String listId = "list456";

        when(followedListService.getUserToFollowedList(userId, listId))
                .thenThrow(new IllegalArgumentException("Followed list not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedListController.unfollowList(userId, listId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Followed list not found.", exception.getReason());
        verify(followedListService, times(1)).getUserToFollowedList(userId, listId);
        verify(followedListService, times(0)).deleteFollowedList(anyString());
    }

    /**
     * Test the getFollowingLists endpoint for a successful retrieval of followed lists.
     */
    @Test
    public void testGetFollowingLists_Success() {
        // Arrange
        String userId = "user123";
        List<String> mockFollowedLists = Arrays.asList("list456", "list789");

        when(followedListService.getFollowedLists(userId)).thenReturn(mockFollowedLists);

        // Act
        ResponseEntity<List<String>> response = followedListController.getFollowingLists(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFollowedLists, response.getBody());
        verify(followedListService, times(1)).getFollowedLists(userId);
    }

    /**
     * Test the getFollowingLists endpoint when an exception occurs.
     */
    @Test
    public void testGetFollowingLists_Failure() {
        // Arrange
        String userId = "user123";

        when(followedListService.getFollowedLists(userId))
                .thenThrow(new IllegalArgumentException("User not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedListController.getFollowingLists(userId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User not found.", exception.getReason());
        verify(followedListService, times(1)).getFollowedLists(userId);
    }

    /**
     * Test the getFollowers endpoint for a successful retrieval of followers.
     */
    @Test
    public void testGetFollowers_Success() {
        // Arrange
        String listId = "list456";
        User follower1 = new User("Follower1", "follower1@example.com", "hashed_pw1", true, false);
        User follower2 = new User("Follower2", "follower2@example.com", "hashed_pw2", false, true);
        List<String> mockFollowers = Arrays.asList(follower1.getId(), follower2.getId());

        when(followedListService.getFollowers(listId)).thenReturn(mockFollowers);

        // Act
        ResponseEntity<List<String>> response = followedListController.getFollowers(listId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFollowers, response.getBody());
        verify(followedListService, times(1)).getFollowers(listId);
    }

    /**
     * Test the getFollowers endpoint when an exception occurs.
     */
    @Test
    public void testGetFollowers_Failure() {
        // Arrange
        String listId = "list456";

        when(followedListService.getFollowers(listId))
                .thenThrow(new IllegalArgumentException("List not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedListController.getFollowers(listId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("List not found.", exception.getReason());
        verify(followedListService, times(1)).getFollowers(listId);
    }

    /**
     * Test the isFollowingList endpoint when the user is following the list.
     */
    @Test
    public void testIsFollowingList_Following() {
        // Arrange
        String userId = "user123";
        String listId = "list456";

        when(followedListService.isFollowingList(userId, listId)).thenReturn(true);

        // Act
        ResponseEntity<String> response = followedListController.isFollowingList(userId, listId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(followedListService, times(1)).isFollowingList(userId, listId);
    }

    /**
     * Test the isFollowingList endpoint when the user is not following the list.
     */
    @Test
    public void testIsFollowingList_NotFollowing() {
        // Arrange
        String userId = "user123";
        String listId = "list456";

        when(followedListService.isFollowingList(userId, listId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = followedListController.isFollowingList(userId, listId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(followedListService, times(1)).isFollowingList(userId, listId);
    }

    /**
     * Test the isFollowingList endpoint when an exception occurs.
     */
    @Test
    public void testIsFollowingList_Failure() {
        // Arrange
        String userId = "user123";
        String listId = "list456";

        when(followedListService.isFollowingList(userId, listId))
                .thenThrow(new IllegalArgumentException("Invalid user or list ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedListController.isFollowingList(userId, listId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid user or list ID.", exception.getReason());
        verify(followedListService, times(1)).isFollowingList(userId, listId);
    }
}
