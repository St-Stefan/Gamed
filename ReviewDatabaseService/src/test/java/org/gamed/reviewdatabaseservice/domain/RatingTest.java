package org.gamed.reviewdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class RatingTest {

    private Rating rating;
    private Review mockReview;

    @BeforeEach
    public void setUp() {
        mockReview = Mockito.mock(Review.class);
        rating = new Rating(mockReview, "Gameplay", 8, "graphData");
    }

    @Test
    public void testRatingCreation() {
        assertNotNull(rating, "Rating object should not be null");
        assertNull(rating.getId(), "ID should be null before persistence");
        assertEquals("Gameplay", rating.getCategory(), "Category should match the constructor argument");
        assertEquals(8, rating.getScore(), "Score should match the constructor argument");
        assertEquals("graphData", rating.getGraph(), "Graph should match the constructor argument");
        assertEquals(mockReview, rating.getReview(), "Review should match the constructor argument");
    }

    @Test
    public void testDefaultConstructor() {
        Rating defaultRating = new Rating();
        assertNotNull(defaultRating, "Default Rating object should not be null");
        assertNull(defaultRating.getId(), "ID should be null");
        assertNull(defaultRating.getCategory(), "Category should be null");
        assertEquals(0, defaultRating.getScore(), "Score should be default to 0");
        assertNull(defaultRating.getGraph(), "Graph should be null");
        assertNull(defaultRating.getReview(), "Review should be null");
    }

    @Test
    public void testSetCategory() {
        rating.setCategory("Graphics");
        assertEquals("Graphics", rating.getCategory(), "Category should be updated to 'Graphics'");
    }

    @Test
    public void testSetScore() {
        rating.setScore(9);
        assertEquals(9, rating.getScore(), "Score should be updated to 9");
    }

    @Test
    public void testSetGraph() {
        rating.setGraph("newGraphData");
        assertEquals("newGraphData", rating.getGraph(), "Graph should be updated to 'newGraphData'");
    }

    @Test
    public void testGetReview() {
        assertEquals(mockReview, rating.getReview(), "getReview() should return the correct Review object");
    }

    @Test
    public void testToString() {
        String expected = "Rating{" +
                "id='" + rating.getId() + '\'' +
                ", review_id='" + mockReview + '\'' +
                ", category='" + rating.getCategory() + '\'' +
                ", score=" + rating.getScore() +
                ", graph='" + rating.getGraph() + '\'' +
                '}';
        assertEquals(expected, rating.toString(), "toString() should return the correct string representation");
    }
}
