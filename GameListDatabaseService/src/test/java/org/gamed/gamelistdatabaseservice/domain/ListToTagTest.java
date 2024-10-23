package org.gamed.gamelistdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class ListToTagTest {

    private ListToTag listToTag;
    private GameList mockList;
    private Tag mockTag;

    @BeforeEach
    public void setUp() {
        // Create mock GameList and Tag objects using Mockito
        mockList = Mockito.mock(GameList.class);
        Mockito.when(mockList.getId()).thenReturn("list123");

        mockTag = Mockito.mock(Tag.class);
        Mockito.when(mockTag.getId()).thenReturn("tag456");

        // Initialize ListToTag with mock GameList and Tag
        listToTag = new ListToTag(mockList.getId(), mockTag.getId());
    }

    @Test
    public void testListToTagCreation() {
        assertNotNull(listToTag, "ListToTag object should not be null");
        assertNull(listToTag.getId(), "ID should be null before persistence");
        assertEquals(mockList.getId(), listToTag.getList(), "List should match the constructor argument");
        assertEquals(mockTag.getId(), listToTag.getTag(), "Tag should match the constructor argument");
    }

    @Test
    public void testDefaultConstructor() {
        ListToTag defaultListToTag = new ListToTag();
        assertNotNull(defaultListToTag, "Default ListToTag object should not be null");
        assertNull(defaultListToTag.getId(), "ID should be null");
        assertNull(defaultListToTag.getList(), "List should be null");
        assertNull(defaultListToTag.getTag(), "Tag should be null");
    }

    @Test
    public void testGetters() {
        assertEquals(mockList.getId(), listToTag.getList(), "getList() should return the correct GameList object");
        assertEquals(mockTag.getId(), listToTag.getTag(), "getTag() should return the correct Tag object");
    }

    @Test
    public void testToString() {
        String expected = "ListToTag{" +
                "id='" + listToTag.getId() + '\'' +
                ", list=" + (listToTag.getList() != null ? listToTag.getList() : null) +
                ", tag=" + (listToTag.getTag() != null ? listToTag.getTag() : null) +
                '}';
        assertEquals(expected, listToTag.toString(), "toString() should return the correct string representation");
    }
}
