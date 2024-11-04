package org.gamed.userdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserToFollowedListTest {

    private UserToFollowedList userToFollowedList;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("TestUser", "test@example.com", "hashed_password", true, false);
        userToFollowedList = new UserToFollowedList(user.getId(), "listId123");
    }

    @Test
    public void testUserToFollowedListCreation() {
        assertNotNull(userToFollowedList);
        assertEquals(user.getId(), userToFollowedList.getUser());
        assertEquals("listId123", userToFollowedList.getListId());
    }
}