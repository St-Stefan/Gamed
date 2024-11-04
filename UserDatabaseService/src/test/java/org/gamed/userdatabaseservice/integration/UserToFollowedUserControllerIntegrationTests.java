package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.controllers.UserToFollowedUserController;
import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToFollowedUser;
import org.gamed.userdatabaseservice.service.UserToFollowedUserService;
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
 * Test class for UserToFollowedUserController.
 */
public class UserToFollowedUserControllerIntegrationTests {

    @Mock
    private UserToFollowedUserService followedUserService;

    @InjectMocks
    private UserToFollowedUserController followedUserController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the followUser endpoint for a successful follow operation.
     */
    @Test
    public void testFollowUser_Success() {
        // Arrange
        String followerId = "user123";
        String followedId = "user456";

        when(followedUserService.createFollowedUser(followerId, followedId)).thenReturn(new UserToFollowedUser());

        // Act
        ResponseEntity<String> response = followedUserController.followUser(followerId, followedId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(followedUserService, times(1)).createFollowedUser(followerId, followedId);
    }

    /**
     * Test the followUser endpoint when an exception occurs.
     */
    @Test
    public void testFollowUser_Failure() {
        // Arrange
        String followerId = "user123";
        String followedId = "user456";

        doThrow(new IllegalArgumentException("Already following this user."))
                .when(followedUserService).createFollowedUser(followerId, followedId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedUserController.followUser(followerId, followedId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Already following this user.", exception.getReason());
        verify(followedUserService, times(1)).createFollowedUser(followerId, followedId);
    }

    /**
     * Test the unfollowUser endpoint for a successful unfollow operation.
     */
    @Test
    public void testUnfollowUser_Success() {
        // Arrange
        String followerId = "user123";
        String followedId = "user456";

        doNothing().when(followedUserService).deleteFollowedUser(followerId, followedId);

        // Act
        ResponseEntity<String> response = followedUserController.unfollowUser(followerId, followedId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(followedUserService, times(1)).deleteFollowedUser(followerId, followedId);
    }

    /**
     * Test the unfollowUser endpoint when the followed user is not found.
     */
    @Test
    public void testUnfollowUser_FollowedUserNotFound() {
        // Arrange
        String followerId = "user123";
        String followedId = "user456";

        doThrow(new IllegalArgumentException("Follow relationship does not exist."))
                .when(followedUserService).deleteFollowedUser(followerId, followedId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedUserController.unfollowUser(followerId, followedId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Follow relationship does not exist.", exception.getReason());
        verify(followedUserService, times(1)).deleteFollowedUser(followerId, followedId);
    }

    /**
     * Test the getFollowing endpoint for a successful retrieval of followed users.
     */
    @Test
    public void testGetFollowing_Success() {
        // Arrange
        String userId = "user123";
        User followedUser1 = new User("UserA", "usera@example.com", "hashed_pw1", true, false);
        User followedUser2 = new User("UserB", "userb@example.com", "hashed_pw2", false, true);
        List<String> mockFollowingList = Arrays.asList(followedUser1.getId(), followedUser2.getId());

        when(followedUserService.getFollowing(userId)).thenReturn(mockFollowingList);

        // Act
        ResponseEntity<List<String>> response = followedUserController.getFollowing(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFollowingList, response.getBody());
        verify(followedUserService, times(1)).getFollowing(userId);
    }

    /**
     * Test the getFollowing endpoint when an exception occurs.
     */
    @Test
    public void testGetFollowing_Failure() {
        // Arrange
        String userId = "user123";

        when(followedUserService.getFollowing(userId))
                .thenThrow(new IllegalArgumentException("User not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedUserController.getFollowing(userId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User not found.", exception.getReason());
        verify(followedUserService, times(1)).getFollowing(userId);
    }

    /**
     * Test the getFollowers endpoint for a successful retrieval of followers.
     */
    @Test
    public void testGetFollowers_Success() {
        // Arrange
        String userId = "user123";
        User follower1 = new User("Follower1", "follower1@example.com", "hashed_pw1", true, false);
        User follower2 = new User("Follower2", "follower2@example.com", "hashed_pw2", false, true);
        List<String> mockFollowersList = Arrays.asList(follower1.getId(), follower2.getId());

        when(followedUserService.getFollowers(userId)).thenReturn(mockFollowersList);

        // Act
        ResponseEntity<List<String>> response = followedUserController.getFollowers(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFollowersList, response.getBody());
        verify(followedUserService, times(1)).getFollowers(userId);
    }

    /**
     * Test the getFollowers endpoint when an exception occurs.
     */
    @Test
    public void testGetFollowers_Failure() {
        // Arrange
        String userId = "user123";

        when(followedUserService.getFollowers(userId))
                .thenThrow(new IllegalArgumentException("User not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedUserController.getFollowers(userId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User not found.", exception.getReason());
        verify(followedUserService, times(1)).getFollowers(userId);
    }

    /**
     * Test the isFollowing endpoint when the follower is following the user.
     */
    @Test
    public void testIsFollowing_Following() {
        // Arrange
        String followerId = "user123";
        String followedId = "user456";

        when(followedUserService.isFollowing(followerId, followedId)).thenReturn(true);

        // Act
        ResponseEntity<String> response = followedUserController.isFollowing(followerId, followedId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(followedUserService, times(1)).isFollowing(followerId, followedId);
    }

    /**
     * Test the isFollowing endpoint when the follower is not following the user.
     */
    @Test
    public void testIsFollowing_NotFollowing() {
        // Arrange
        String followerId = "user123";
        String followedId = "user456";

        when(followedUserService.isFollowing(followerId, followedId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = followedUserController.isFollowing(followerId, followedId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(followedUserService, times(1)).isFollowing(followerId, followedId);
    }

    /**
     * Test the isFollowing endpoint when an exception occurs.
     */
    @Test
    public void testIsFollowing_Failure() {
        // Arrange
        String followerId = "user123";
        String followedId = "user456";

        when(followedUserService.isFollowing(followerId, followedId))
                .thenThrow(new IllegalArgumentException("Invalid follower or followed ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followedUserController.isFollowing(followerId, followedId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid follower or followed ID.", exception.getReason());
        verify(followedUserService, times(1)).isFollowing(followerId, followedId);
    }
}
