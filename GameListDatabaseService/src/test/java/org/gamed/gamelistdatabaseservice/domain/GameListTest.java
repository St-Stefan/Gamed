package org.gamed.gamelistdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class GameListTest {

    private GameList gameList;
    private LocalDateTime creationTime;
    private LocalDateTime modificationTime;

    @BeforeEach
    public void setUp() {
        gameList = new GameList("user123", "My Favorite Games", "A list of my all-time favorite games.");
    }

    @Test
    public void testGameListCreation() {
        assertNotNull(gameList, "GameList object should not be null");
        assertNull(gameList.getId(), "ID should be null before persistence");
        assertEquals("user123", gameList.getUserId(), "User ID should match the constructor argument");
        assertEquals("My Favorite Games", gameList.getName(), "Name should match the constructor argument");
        assertEquals("A list of my all-time favorite games.", gameList.getDescription(), "Description should match the constructor argument");
        assertNull(gameList.getTimeCreated(), "Creation time should be null before persistence");
        assertNull(gameList.getTimeModified(), "Modification time should be null before persistence");
    }

    @Test
    public void testDefaultConstructor() {
        GameList defaultGameList = new GameList();
        assertNotNull(defaultGameList, "Default GameList object should not be null");
        assertNull(defaultGameList.getId(), "ID should be null");
        assertNull(defaultGameList.getUserId(), "User ID should be null");
        assertNull(defaultGameList.getName(), "Name should be null");
        assertNull(defaultGameList.getDescription(), "Description should be null");
        assertNull(defaultGameList.getTimeCreated(), "Creation time should be null");
        assertNull(defaultGameList.getTimeModified(), "Modification time should be null");
    }

    @Test
    public void testSetName() {
        gameList.setName("Updated Game List");
        assertEquals("Updated Game List", gameList.getName(), "Name should be updated to 'Updated Game List'");
    }

    @Test
    public void testSetDescription() {
        gameList.setDescription("An updated description for my game list.");
        assertEquals("An updated description for my game list.", gameList.getDescription(), "Description should be updated");
    }

    @Test
    public void testGetters() {
        assertEquals("user123", gameList.getUserId(), "getUserId() should return the correct user ID");
        assertEquals("My Favorite Games", gameList.getName(), "getName() should return the correct name");
        assertEquals("A list of my all-time favorite games.", gameList.getDescription(), "getDescription() should return the correct description");
    }

    @Test
    public void testToString() {
        String expected = "List{" +
                "id='" + gameList.getId() + '\'' +
                ", user='" + gameList.getUserId() + '\'' +
                ", name='" + gameList.getName() + '\'' +
                ", description='" + gameList.getDescription() + '\'' +
                ", time_created=" + gameList.getTimeCreated() +
                ", time_modified=" + gameList.getTimeModified() +
                '}';
        assertEquals(expected, gameList.toString(), "toString() should return the correct string representation");
    }
}
