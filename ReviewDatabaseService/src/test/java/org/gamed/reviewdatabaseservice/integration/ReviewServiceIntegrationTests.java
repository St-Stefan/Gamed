package org.gamed.reviewdatabaseservice.integration;

import org.gamed.reviewdatabaseservice.domain.Review;
import org.gamed.reviewdatabaseservice.repository.ReviewRepository;
import org.gamed.reviewdatabaseservice.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceIntegrationTests {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void testCreateReviewSuccess() {
        String userId = "user123";
        String gameId = "game456";
        String description = "This is a review";

        when(reviewRepository.existsByUserIdAndGameId(userId, gameId)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review result = reviewService.createReview(userId, gameId, description);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(gameId, result.getGameId());
        assertEquals(description, result.getDescription());

        verify(reviewRepository).existsByUserIdAndGameId(userId, gameId);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void testCreateReviewThrowsExceptionWhenDescriptionIsNull() {
        String userId = "user123";
        String gameId = "game456";
        String description = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.createReview(userId, gameId, description)
        );

        assertEquals("Description cannot be blank.", exception.getMessage());

        verify(reviewRepository, never()).existsByUserIdAndGameId(anyString(), anyString());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testCreateReviewThrowsExceptionWhenDescriptionIsEmpty() {
        String userId = "user123";
        String gameId = "game456";
        String description = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.createReview(userId, gameId, description)
        );

        assertEquals("Description cannot be blank.", exception.getMessage());

        verify(reviewRepository, never()).existsByUserIdAndGameId(anyString(), anyString());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testCreateReviewThrowsExceptionWhenRelationAlreadyExists() {
        String userId = "user123";
        String gameId = "game456";
        String description = "This is a review";

        when(reviewRepository.existsByUserIdAndGameId(userId, gameId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.createReview(userId, gameId, description)
        );

        assertEquals("Relation already exists.", exception.getMessage());

        verify(reviewRepository).existsByUserIdAndGameId(userId, gameId);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testGetReviewThrowsExceptionWhenNotFound() {
        String id = "review123";

        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.getReview(id)
        );

        assertEquals("Given Review id does not correspond to any Review. ID: " + id, exception.getMessage());

        verify(reviewRepository).findById(id);
    }

    @Test
    void testUpdateReviewSuccess() {
        String id = "review123";
        String newDescription = "Updated review description";
        Review existingReview = new Review("user123", "game456", "Old description");

        when(reviewRepository.findById(id)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(existingReview)).thenReturn(existingReview);

        Review result = reviewService.updateReview(id, newDescription);

        assertNotNull(result);
        assertEquals(newDescription, result.getDescription());

        verify(reviewRepository).findById(id);
        verify(reviewRepository).save(existingReview);
    }

    @Test
    void testUpdateReviewThrowsExceptionWhenDescriptionIsEmpty() {
        String id = "review123";
        String newDescription = "";
        Review existingReview = new Review("user123", "game456", "Old description");

        when(reviewRepository.findById(id)).thenReturn(Optional.of(existingReview));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.updateReview(id, newDescription)
        );

        assertEquals("Description cannot be blank.", exception.getMessage());

        verify(reviewRepository).findById(id);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testUpdateReviewThrowsExceptionWhenNotFound() {
        String id = "review123";
        String newDescription = "Updated review description";

        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.updateReview(id, newDescription)
        );

        assertEquals("Given Review id does not correspond to any Review. ID: " + id, exception.getMessage());

        verify(reviewRepository).findById(id);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testDeleteReviewSuccess() {
        String id = "review123";

        when(reviewRepository.existsById(id)).thenReturn(true);

        reviewService.deleteReview(id);

        verify(reviewRepository).existsById(id);
        verify(reviewRepository).deleteById(id);
    }

    @Test
    void testDeleteReviewThrowsExceptionWhenNotFound() {
        String id = "review123";

        when(reviewRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.deleteReview(id)
        );

        assertEquals("Given Review id does not correspond to any Review. ID: " + id, exception.getMessage());

        verify(reviewRepository).existsById(id);
        verify(reviewRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetReviewsByGameId() {
        String gameId = "game456";
        List<Review> reviews = Arrays.asList(
                new Review("user1", gameId, "Review 1"),
                new Review("user2", gameId, "Review 2")
        );

        when(reviewRepository.getReviewsByGameId(gameId)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByGameId(gameId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(reviews, result);

        verify(reviewRepository).getReviewsByGameId(gameId);
    }

    @Test
    void testGetReviewsByUserId() {
        String userId = "user123";
        List<Review> reviews = Arrays.asList(
                new Review(userId, "game1", "Review 1"),
                new Review(userId, "game2", "Review 2")
        );

        when(reviewRepository.getReviewsByUserId(userId)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(reviews, result);

        verify(reviewRepository).getReviewsByUserId(userId);
    }
}
