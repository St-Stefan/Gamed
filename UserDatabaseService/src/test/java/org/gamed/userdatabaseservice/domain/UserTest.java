package org.gamed.userdatabaseservice.domain;

import org.gamed.userdatabaseservice.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("TestUser", "test@example.com", "hashed_password", true, false);
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user);
        assertEquals("TestUser", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertTrue(user.isDeveloper());
        assertFalse(user.isPremium());
        assertNull(user.getTimeCreated());
        assertNull(user.getTimeModified());
    }
}