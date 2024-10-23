package org.gamed.gamelistdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    private Tag tag;

    @BeforeEach
    public void setUp() {
        tag = new Tag("Adventure", LocalDateTime.of(2023,1,1,1,0,0));
    }

    @Test
    public void testTagCreation() {
        assertNotNull(tag, "Tag object should not be null");
        assertNull(tag.getId(), "ID should be null before persistence");
        assertEquals("Adventure", tag.getName(), "Name should match the constructor argument");
        assertEquals(LocalDateTime.of(2023,1,1,1,0,0), tag.getTimeCreated(), "Time_created should match the constructor argument");
    }

    @Test
    public void testDefaultConstructor() {
        Tag defaultTag = new Tag();
        assertNotNull(defaultTag, "Default Tag object should not be null");
        assertNull(defaultTag.getId(), "ID should be null");
        assertNull(defaultTag.getName(), "Name should be null");
        assertEquals(null, defaultTag.getTimeCreated(), "Time_created should default to 0");
    }

    @Test
    public void testGetters() {
        assertEquals("Adventure", tag.getName(), "getName() should return the correct name");
        assertEquals(LocalDateTime.of(2023,1,1,1,0,0), tag.getTimeCreated(), "getTimeCreated() should return the correct timestamp");
    }

    @Test
    public void testToString() {
        String expected = "Tag{" +
                "id='" + tag.getId() + '\'' +
                ", name='" + tag.getName() + '\'' +
                ", time_created=" + tag.getTimeCreated() +
                '}';
        assertEquals(expected, tag.toString(), "toString() should return the correct string representation");
    }
}
