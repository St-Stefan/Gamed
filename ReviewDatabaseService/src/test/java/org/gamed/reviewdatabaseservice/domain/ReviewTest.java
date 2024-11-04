package org.gamed.reviewdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class ReviewTest {

    private Review review;

    @BeforeEach
    public void setUp() {
        review = new Review("user123", "game456", "This is a review.");
    }

    @Test
    public void testReviewCreation() {
        assertNotNull(review, "Review object should not be null");
        assertNull(review.getId(), "ID should be null before persistence");
        assertEquals("user123", review.getUserId(), "User ID should match the constructor argument");
        assertEquals("game456", review.getGameId(), "Game ID should match the constructor argument");
        assertEquals("This is a review.", review.getDescription(), "Description should match the constructor argument");
        assertNull(review.getTimeCreated(), "Creation time should be null before persistence");
        assertNull(review.getTimeModified(), "Modification time should be null before persistence");
    }

    @Test
    public void testDefaultConstructor() {
        Review defaultReview = new Review();
        assertNotNull(defaultReview, "Default Review object should not be null");
        assertNull(defaultReview.getId(), "ID should be null");
        assertNull(defaultReview.getUserId(), "User ID should be null");
        assertNull(defaultReview.getGameId(), "Game ID should be null");
        assertNull(defaultReview.getDescription(), "Description should be null");
        assertNull(defaultReview.getTimeCreated(), "Creation time should be null");
        assertNull(defaultReview.getTimeModified(), "Modification time should be null");
    }

    @Test
    public void testSetDescription() {
        review.setDescription("Updated review.");
        assertEquals("Updated review.", review.getDescription(), "Description should be updated");
    }

    @Test
    public void testGetters() {
        assertEquals("user123", review.getUserId(), "getUserId() should return the correct user ID");
        assertEquals("game456", review.getGameId(), "getGameId() should return the correct game ID");
        assertEquals("This is a review.", review.getDescription(), "getDescription() should return the correct description");
    }

    @Test
    public void testToString() {
        String expected = "Review{" +
                "id='" + review.getId() + '\'' +
                ", user_id='" + review.getUserId() + '\'' +
                ", game_id='" + review.getGameId() + '\'' +
                ", description='" + review.getDescription() + '\'' +
                ", time_created=" + review.getTimeCreated() +
                ", time_modified=" + review.getTimeModified() +
                '}';
        assertEquals(expected, review.toString(), "toString() should return the correct string representation");
    }
}
