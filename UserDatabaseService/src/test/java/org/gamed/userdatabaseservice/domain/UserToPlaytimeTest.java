package org.gamed.userdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserToPlaytimeTest {

    private UserToPlaytime userToPlaytime;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("TestUser", "test@example.com", "hashed_password", true, false);
        userToPlaytime = new UserToPlaytime(user.getId(), "gameId123", 100);
    }

    @Test
    public void testUserToPlaytimeCreation() {
        assertNotNull(userToPlaytime);
        assertEquals(user.getId(), userToPlaytime.getUser());
        assertEquals("gameId123", userToPlaytime.getGameId());
        assertEquals(100, userToPlaytime.getPlaytime());
    }

    @Test
    public void testUserPlaytimeUpdate() {
        userToPlaytime.setPlaytime(200);
        assertEquals(200, userToPlaytime.getPlaytime());
    }
}