package org.gamed.userdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserToLikeTest {

    private UserToLike userToLike;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("TestUser", "test@example.com", "hashed_password", true, false);
        userToLike = new UserToLike(user.getId(), "item123", "love");
    }

    @Test
    public void testUserToLikeCreation() {
        assertNotNull(userToLike);
        assertEquals(user.getId(), userToLike.getUser());
        assertEquals("item123", userToLike.getItemId());
    }
}
