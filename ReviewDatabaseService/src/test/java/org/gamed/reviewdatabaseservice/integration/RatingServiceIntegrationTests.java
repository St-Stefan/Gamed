package org.gamed.reviewdatabaseservice.integration;

import org.gamed.reviewdatabaseservice.domain.Rating;
import org.gamed.reviewdatabaseservice.domain.Review;
import org.gamed.reviewdatabaseservice.repository.RatingRepository;
import org.gamed.reviewdatabaseservice.repository.ReviewRepository;
import org.gamed.reviewdatabaseservice.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceIntegrationTests {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void testCreateRatingThrowsExceptionWhenCategoryIsBlank() {
        String reviewId = "review123";
        String category = "";
        int score = 8;
        String graph = "graphData";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ratingService.createRating(reviewId, category, score, graph)
        );

        assertEquals("Category cannot be blank.", exception.getMessage());

        verify(ratingRepository, never()).existsByReviewIdAndCategory(anyString(), anyString());
        verify(reviewRepository, never()).getReviewById(anyString());
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testCreateRatingThrowsExceptionWhenScoreOutOfRange() {
        String reviewId = "review123";
        String category = "Graphics";
        int score = 11; // Invalid score
        String graph = "graphData";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ratingService.createRating(reviewId, category, score, graph)
        );

        assertEquals("Score must be between 0 and 10.", exception.getMessage());

        verify(ratingRepository, never()).existsByReviewIdAndCategory(anyString(), anyString());
        verify(reviewRepository, never()).getReviewById(anyString());
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testCreateRatingThrowsExceptionWhenRelationAlreadyExists() {
        String reviewId = "review123";
        String category = "Graphics";
        int score = 8;
        String graph = "graphData";

        when(ratingRepository.existsByReviewIdAndCategory(reviewId, category)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ratingService.createRating(reviewId, category, score, graph)
        );

        assertEquals("Relation already exists.", exception.getMessage());

        verify(ratingRepository).existsByReviewIdAndCategory(reviewId, category);
        verify(reviewRepository, never()).getReviewById(anyString());
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testGetRatingSuccess() {
        String id = "rating123";
        Rating rating = new Rating();

        when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));

        Rating result = ratingService.getRating(id);

        assertNotNull(result);
        assertEquals(rating, result);

        verify(ratingRepository).findById(id);
    }

    @Test
    void testGetRatingThrowsExceptionWhenNotFound() {
        String id = "rating123";

        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ratingService.getRating(id)
        );

        assertEquals("Given Rating id does not correspond to any Rating. ID: " + id, exception.getMessage());

        verify(ratingRepository).findById(id);
    }

    @Test
    void testUpdateRatingSuccess() {
        String id = "rating123";
        String newCategory = "Sound";
        int newScore = 9;
        String newGraph = "newGraphData";

        Rating existingRating = new Rating();
        existingRating.setCategory("Graphics");
        existingRating.setScore(8);
        existingRating.setGraph("oldGraphData");

        when(ratingRepository.findById(id)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(existingRating)).thenReturn(existingRating);

        Rating result = ratingService.updateRating(id, newCategory, newScore, newGraph);

        assertNotNull(result);
        assertEquals(newCategory, result.getCategory());
        assertEquals(newScore, result.getScore());
        assertEquals(newGraph, result.getGraph());

        verify(ratingRepository).findById(id);
        verify(ratingRepository).save(existingRating);
    }

    @Test
    void testUpdateRatingThrowsExceptionWhenCategoryIsBlank() {
        String id = "rating123";
        String newCategory = "";
        int newScore = 9;
        String newGraph = "newGraphData";

        Rating existingRating = new Rating();

        when(ratingRepository.findById(id)).thenReturn(Optional.of(existingRating));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ratingService.updateRating(id, newCategory, newScore, newGraph)
        );

        assertEquals("Category cannot be blank.", exception.getMessage());

        verify(ratingRepository).findById(id);
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testUpdateRatingThrowsExceptionWhenScoreOutOfRange() {
        String id = "rating123";
        String newCategory = "Gameplay";
        int newScore = -1; // Invalid score
        String newGraph = "newGraphData";

        Rating existingRating = new Rating();

        when(ratingRepository.findById(id)).thenReturn(Optional.of(existingRating));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ratingService.updateRating(id, newCategory, newScore, newGraph)
        );

        assertEquals("Score must be between 0 and 10.", exception.getMessage());

        verify(ratingRepository).findById(id);
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testUpdateRatingThrowsExceptionWhenNotFound() {
        String id = "rating123";
        String newCategory = "Gameplay";
        int newScore = 7;
        String newGraph = "newGraphData";

        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ratingService.updateRating(id, newCategory, newScore, newGraph)
        );

        assertEquals("Given Rating id does not correspond to any Rating. ID: " + id, exception.getMessage());

        verify(ratingRepository).findById(id);
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testDeleteRatingSuccess() {
        String id = "rating123";

        when(ratingRepository.existsById(id)).thenReturn(true);

        ratingService.deleteRating(id);

        verify(ratingRepository).existsById(id);
        verify(ratingRepository).deleteById(id);
    }

    @Test
    void testDeleteRatingThrowsExceptionWhenNotFound() {
        String id = "rating123";

        when(ratingRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ratingService.deleteRating(id)
        );

        assertEquals("Given Rating id does not correspond to any Rating. ID: " + id, exception.getMessage());

        verify(ratingRepository).existsById(id);
        verify(ratingRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetRatingsByReviewId() {
        String reviewId = "review123";
        List<Rating> ratings = Arrays.asList(
                new Rating(new Review(), "Graphics", 8, "graph1"),
                new Rating(new Review(), "Gameplay", 9, "graph2")
        );

        when(ratingRepository.getRatingsByReviewId(reviewId)).thenReturn(ratings);

        List<Rating> result = ratingService.getRatingsByReviewId(reviewId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ratings, result);

        verify(ratingRepository).getRatingsByReviewId(reviewId);
    }
}
