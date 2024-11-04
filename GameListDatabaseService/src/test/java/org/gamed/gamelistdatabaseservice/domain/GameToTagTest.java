package org.gamed.gamelistdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class GameToTagTest {

    private GameToTag gameToTag;
    private Game mockGame;
    private Tag mockTag;

    @BeforeEach
    public void setUp() {
        // Create mock Game and Tag objects using Mockito
        mockGame = Mockito.mock(Game.class);
        Mockito.when(mockGame.getId()).thenReturn("game123");

        mockTag = Mockito.mock(Tag.class);
        Mockito.when(mockTag.getId()).thenReturn("tag456");

        // Initialize GameToTag with mock Game and Tag
        gameToTag = new GameToTag(mockGame.getId(), mockTag.getId());
    }

    @Test
    public void testGameToTagCreation() {
        assertNotNull(gameToTag, "GameToTag object should not be null");
        assertNull(gameToTag.getId(), "ID should be null before persistence");
        assertEquals(mockGame.getId(), gameToTag.getGame(), "Game should match the constructor argument");
        assertEquals(mockTag.getId(), gameToTag.getTag(), "Tag should match the constructor argument");
        assertEquals(0, gameToTag.getOccurrences(), "Occurrences should default to 0 before persistence");
    }

    @Test
    public void testDefaultConstructor() {
        GameToTag defaultGameToTag = new GameToTag();
        assertNotNull(defaultGameToTag, "Default GameToTag object should not be null");
        assertNull(defaultGameToTag.getId(), "ID should be null");
        assertNull(defaultGameToTag.getGame(), "Game should be null");
        assertNull(defaultGameToTag.getTag(), "Tag should be null");
        assertEquals(0, defaultGameToTag.getOccurrences(), "Occurrences should default to 0");
    }

    @Test
    public void testGetters() {
        assertEquals(mockGame.getId(), gameToTag.getGame(), "getGame() should return the correct Game object");
        assertEquals(mockTag.getId(), gameToTag.getTag(), "getTag() should return the correct Tag object");
        assertEquals(0, gameToTag.getOccurrences(), "getOccurrences() should return the correct default value");
    }

    @Test
    public void testToString() {
        String expected = "GameToTag{" +
                "id='" + gameToTag.getId() + '\'' +
                ", game=" + (gameToTag.getGame() != null ? gameToTag.getGame() : null) +
                ", tag=" + (gameToTag.getTag() != null ? gameToTag.getTag() : null) +
                ", occurrences=" + gameToTag.getOccurrences() +
                '}';
        assertEquals(expected, gameToTag.toString(), "toString() should return the correct string representation");
    }
}
