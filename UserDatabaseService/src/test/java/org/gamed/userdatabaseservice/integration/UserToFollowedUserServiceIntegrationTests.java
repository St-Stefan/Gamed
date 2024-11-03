package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToFollowedUser;
import org.gamed.userdatabaseservice.repository.UserRepository;
import org.gamed.userdatabaseservice.repository.UserToFollowedUserRepository;
import org.gamed.userdatabaseservice.service.UserToFollowedUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserToFollowedUserServiceIntegrationTests {

    @Mock
    private UserToFollowedUserRepository followedUserRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserToFollowedUserService userToFollowedUserService;

    @Test
    void testCreateFollowedUserSuccess() {
        String userId = "user123";
        String followedUserId = "user456";

        when(followedUserRepository.findByUserIdAndFollowedUserId(userId, followedUserId))
                .thenReturn(Optional.empty());
        when(followedUserRepository.save(any(UserToFollowedUser.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserToFollowedUser result = userToFollowedUserService.createFollowedUser(userId, followedUserId);

        assertNotNull(result);
        assertEquals(userId, result.getUser());
        assertEquals(followedUserId, result.getFollowedId());

        verify(followedUserRepository).findByUserIdAndFollowedUserId(userId, followedUserId);
        verify(followedUserRepository).save(any(UserToFollowedUser.class));
    }

    @Test
    void testCreateFollowedUserThrowsExceptionWhenUserIdIsNull() {
        String userId = null;
        String followedUserId = "user456";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedUserService.createFollowedUser(userId, followedUserId)
        );

        assertEquals("User ids cannot be null.", exception.getMessage());

        verify(followedUserRepository, never()).findByUserIdAndFollowedUserId(anyString(), anyString());
        verify(followedUserRepository, never()).save(any(UserToFollowedUser.class));
    }

    @Test
    void testCreateFollowedUserThrowsExceptionWhenFollowedUserIdIsNull() {
        String userId = "user123";
        String followedUserId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedUserService.createFollowedUser(userId, followedUserId)
        );

        assertEquals("User ids cannot be null.", exception.getMessage());

        verify(followedUserRepository, never()).findByUserIdAndFollowedUserId(anyString(), anyString());
        verify(followedUserRepository, never()).save(any(UserToFollowedUser.class));
    }

    @Test
    void testCreateFollowedUserThrowsExceptionWhenRelationAlreadyExists() {
        String userId = "user123";
        String followedUserId = "user456";

        when(followedUserRepository.findByUserIdAndFollowedUserId(userId, followedUserId))
                .thenReturn(Optional.of(new UserToFollowedUser(userId, followedUserId)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedUserService.createFollowedUser(userId, followedUserId)
        );

        assertEquals("Relation already exists.", exception.getMessage());

        verify(followedUserRepository).findByUserIdAndFollowedUserId(userId, followedUserId);
        verify(followedUserRepository, never()).save(any(UserToFollowedUser.class));
    }

    @Test
    void testGetFollowedUserSuccess() {
        String id = "relation123";
        UserToFollowedUser relation = new UserToFollowedUser("user123", "user456");

        when(followedUserRepository.findById(id)).thenReturn(Optional.of(relation));

        UserToFollowedUser result = userToFollowedUserService.getFollowedUser(id);

        assertNotNull(result);
        assertEquals(relation, result);

        verify(followedUserRepository).findById(id);
    }

    @Test
    void testGetFollowedUserThrowsExceptionWhenNotFound() {
        String id = "relation123";

        when(followedUserRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedUserService.getFollowedUser(id)
        );

        assertEquals("Given UserToFollowedUser id does not correspond to any UserToFollowedUser. ID: " + id, exception.getMessage());

        verify(followedUserRepository).findById(id);
    }

    @Test
    void testGetFollowersThrowsExceptionWhenUserNotFound() {
        String userId = "user456";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedUserService.getFollowers(userId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(followedUserRepository, never()).findFollowers(anyString());
    }

    @Test
    void testGetFollowingThrowsExceptionWhenUserNotFound() {
        String userId = "user123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedUserService.getFollowing(userId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(followedUserRepository, never()).findFollowed(anyString());
    }

    @Test
    void testIsFollowingReturnsTrueWhenFollowing() {
        String followerId = "user123";
        String followedId = "user456";

        when(followedUserRepository.findByUserIdAndFollowedUserId(followerId, followedId))
                .thenReturn(Optional.of(new UserToFollowedUser(followerId, followedId)));

        boolean result = userToFollowedUserService.isFollowing(followerId, followedId);

        assertTrue(result);

        verify(followedUserRepository).findByUserIdAndFollowedUserId(followerId, followedId);
    }

    @Test
    void testIsFollowingReturnsFalseWhenNotFollowing() {
        String followerId = "user123";
        String followedId = "user456";

        when(followedUserRepository.findByUserIdAndFollowedUserId(followerId, followedId))
                .thenReturn(Optional.empty());

        boolean result = userToFollowedUserService.isFollowing(followerId, followedId);

        assertFalse(result);

        verify(followedUserRepository).findByUserIdAndFollowedUserId(followerId, followedId);
    }

    @Test
    void testDeleteFollowedUserSuccess() {
        String followerId = "user123";
        String followedId = "user456";
        String relationId = "relation789";

        UserToFollowedUser relation = new UserToFollowedUser(followerId, followedId);

        when(followedUserRepository.findByUserIdAndFollowedUserId(followerId, followedId))
                .thenReturn(Optional.of(relation));

        userToFollowedUserService.deleteFollowedUser(followerId, followedId);

        verify(followedUserRepository).findByUserIdAndFollowedUserId(followerId, followedId);
    }

    @Test
    void testDeleteFollowedUserThrowsExceptionWhenNotFound() {
        String followerId = "user123";
        String followedId = "user456";

        when(followedUserRepository.findByUserIdAndFollowedUserId(followerId, followedId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userToFollowedUserService.deleteFollowedUser(followerId, followedId)
        );

        assertEquals("No UserToFollowedUser found for followerId: " + followerId + " and followedId: " + followedId, exception.getMessage());

        verify(followedUserRepository).findByUserIdAndFollowedUserId(followerId, followedId);
        verify(followedUserRepository, never()).deleteById(anyString());
    }
}
