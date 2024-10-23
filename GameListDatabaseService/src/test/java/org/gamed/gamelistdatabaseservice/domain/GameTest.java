package org.gamed.gamelistdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class GameTest {

    private Game game;
    private LocalDateTime releaseDate;

    @BeforeEach
    public void setUp() {
        releaseDate = LocalDateTime.of(2023, 9, 15, 10, 30);
        game = new Game("Epic Adventure", "GameStudio", releaseDate, "PC, PS5, Xbox");
    }

    @Test
    public void testGameCreation() {
        assertNotNull(game, "Game object should not be null");
        assertNull(game.getId(), "ID should be null before persistence");
        assertEquals("Epic Adventure", game.getName(), "Name should match the constructor argument");
        assertEquals("GameStudio", game.getDeveloper(), "Developer should match the constructor argument");
        assertEquals(releaseDate, game.getReleaseDate(), "Release date should match the constructor argument");
        assertEquals("PC, PS5, Xbox", game.getPlatforms(), "Platforms should match the constructor argument");
    }

    @Test
    public void testDefaultConstructor() {
        Game defaultGame = new Game();
        assertNotNull(defaultGame, "Default Game object should not be null");
        assertNull(defaultGame.getId(), "ID should be null");
        assertNull(defaultGame.getName(), "Name should be null");
        assertNull(defaultGame.getDeveloper(), "Developer should be null");
        assertNull(defaultGame.getReleaseDate(), "Release date should be null");
        assertNull(defaultGame.getPlatforms(), "Platforms should be null");
    }

    @Test
    public void testSetReleaseDate() {
        LocalDateTime newReleaseDate = LocalDateTime.of(2024, 1, 20, 15, 45);
        game.setReleaseDate(newReleaseDate);
        assertEquals(newReleaseDate, game.getReleaseDate(), "Release date should be updated to the new value");
    }

    @Test
    public void testSetPlatforms() {
        game.setPlatforms("Nintendo Switch, PC");
        assertEquals("Nintendo Switch, PC", game.getPlatforms(), "Platforms should be updated to the new value");
    }

    @Test
    public void testGetters() {
        assertEquals("Epic Adventure", game.getName(), "getName() should return the correct name");
        assertEquals("GameStudio", game.getDeveloper(), "getDeveloper() should return the correct developer");
        assertEquals(releaseDate, game.getReleaseDate(), "getReleaseDate() should return the correct release date");
        assertEquals("PC, PS5, Xbox", game.getPlatforms(), "getPlatforms() should return the correct platforms");
    }

    @Test
    public void testToString() {
        String expected = "Game{" +
                "id='" + game.getId() + '\'' +
                ", name='" + game.getName() + '\'' +
                ", developer='" + game.getDeveloper() + '\'' +
                ", release_date=\'" + game.getReleaseDate() + '\'' +
                ", platforms='" + game.getPlatforms() + '\'' +
                '}';
        assertEquals(expected, game.toString(), "toString() should return the correct string representation");
    }
}
