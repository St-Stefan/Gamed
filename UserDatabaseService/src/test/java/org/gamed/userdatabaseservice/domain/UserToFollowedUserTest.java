package org.gamed.userdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserToFollowedUserTest {

    private UserToFollowedUser userToFollowedUser;
    private User user;
    private User followedUser;

    @BeforeEach
    public void setUp() {
        user = new User("TestUser", "test@example.com", "hashed_password", true, false);
        followedUser = new User("FollowedUser", "followed@example.com", "hashed_password", false, false);
        userToFollowedUser = new UserToFollowedUser(user.getId(), followedUser.getId());
    }

    @Test
    public void testUserToFollowedUserCreation() {
        assertNotNull(userToFollowedUser);
        assertEquals(user.getId(), userToFollowedUser.getUser());
        assertNull(userToFollowedUser.getFollowedId());
    }
}