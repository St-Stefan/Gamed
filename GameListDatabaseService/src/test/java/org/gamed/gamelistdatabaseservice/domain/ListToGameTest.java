package org.gamed.gamelistdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class ListToGameTest {

    private ListToGame listToGame;
    private GameList mockList;
    private Game mockGame;

    @BeforeEach
    public void setUp() {
        // Create mock GameList and Game objects using Mockito
        mockList = Mockito.mock(GameList.class);
        Mockito.when(mockList.getId()).thenReturn("list123");

        mockGame = Mockito.mock(Game.class);
        Mockito.when(mockGame.getId()).thenReturn("game456");

        // Initialize ListToGame with mock GameList and Game
        listToGame = new ListToGame(mockList.getId(), mockGame.getId());
    }

    @Test
    public void testListToGameCreation() {
        assertNotNull(listToGame, "ListToGame object should not be null");
        assertNull(listToGame.getId(), "ID should be null before persistence");
        assertEquals(mockList.getId(), listToGame.getList(), "List should match the constructor argument");
        assertEquals(mockGame.getId(), listToGame.getGame(), "Game should match the constructor argument");
    }

    @Test
    public void testDefaultConstructor() {
        ListToGame defaultListToGame = new ListToGame();
        assertNotNull(defaultListToGame, "Default ListToGame object should not be null");
        assertNull(defaultListToGame.getId(), "ID should be null");
        assertNull(defaultListToGame.getList(), "List should be null");
        assertNull(defaultListToGame.getGame(), "Game should be null");
    }

    @Test
    public void testGetters() {
        assertEquals(mockList.getId(), listToGame.getList(), "getList() should return the correct GameList object");
        assertEquals(mockGame.getId(), listToGame.getGame(), "getGame() should return the correct Game object");
    }

    @Test
    public void testToString() {
        String expected = "ListToGame{" +
                "id='" + listToGame.getId() + '\'' +
                ", list=" + (listToGame.getList() != null ? listToGame.getList() : null) +
                ", game=" + (listToGame.getGame() != null ? listToGame.getGame() : null) +
                '}';
        assertEquals(expected, listToGame.toString(), "toString() should return the correct string representation");
    }
}
