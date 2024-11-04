package org.gamed.reviewdatabaseservice.integration;

import org.gamed.reviewdatabaseservice.controllers.ReviewController;
import org.gamed.reviewdatabaseservice.domain.Review;
import org.gamed.reviewdatabaseservice.models.CreateAndUpdateReviewRequestModel;
import org.gamed.reviewdatabaseservice.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ReviewController.
 */
public class ReviewControllerIntegrationTests {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createReview endpoint for a successful review creation.
     */
    @Test
    public void testCreateReview_Success() {
        // Arrange
        CreateAndUpdateReviewRequestModel request = new CreateAndUpdateReviewRequestModel(
                "user123",
                "game456",
                "This is a great game!"
        );

        Review mockReview = new Review("user123", "game456", "This is a great game!");

        when(reviewService.createReview(
                request.getUserId(),
                request.getGameId(),
                request.getDescription()
        )).thenReturn(mockReview);

        // Act
        ResponseEntity<String> response = reviewController.createReview(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(reviewService, times(1)).createReview(
                request.getUserId(),
                request.getGameId(),
                request.getDescription()
        );
    }

    /**
     * Test the createReview endpoint when an exception occurs.
     */
    @Test
    public void testCreateReview_Failure() {
        // Arrange
        CreateAndUpdateReviewRequestModel request = new CreateAndUpdateReviewRequestModel(
                "user123",
                "game456",
                "This is a great game!"
        );

        when(reviewService.createReview(
                request.getUserId(),
                request.getGameId(),
                request.getDescription()
        )).thenThrow(new IllegalArgumentException("Invalid review data."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewController.createReview(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid review data.", exception.getReason());
        verify(reviewService, times(1)).createReview(
                request.getUserId(),
                request.getGameId(),
                request.getDescription()
        );
    }

    /**
     * Test the updateReview endpoint for a successful review update.
     */
    @Test
    public void testUpdateReview_Success() {
        // Arrange
        String reviewId = "review789";
        CreateAndUpdateReviewRequestModel request = new CreateAndUpdateReviewRequestModel(
                "user123",
                "game456",
                "Updated review description."
        );

        when(reviewService.updateReview(reviewId, request.getDescription())).thenReturn(new Review());

        // Act
        HttpStatus response = reviewController.updateReview(reviewId, request);

        // Assert
        assertEquals(HttpStatus.OK, response);
        verify(reviewService, times(1)).updateReview(reviewId, request.getDescription());
    }

    /**
     * Test the updateReview endpoint when the review does not exist.
     */
    @Test
    public void testUpdateReview_ReviewNotFound() {
        // Arrange
        String reviewId = "nonexistentReview";
        CreateAndUpdateReviewRequestModel request = new CreateAndUpdateReviewRequestModel(
                "user123",
                "game456",
                "Updated review description."
        );

        doThrow(new IllegalArgumentException("Review not found."))
                .when(reviewService).updateReview(reviewId, request.getDescription());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewController.updateReview(reviewId, request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Review not found.", exception.getReason());
        verify(reviewService, times(1)).updateReview(reviewId, request.getDescription());
    }

    /**
     * Test the getReview endpoint for a successful retrieval of a review.
     */
    @Test
    public void testGetReview_Success() {
        // Arrange
        String reviewId = "review789";
        Review mockReview = new Review("user123", "game456", "This is a great game!");

        when(reviewService.getReview(reviewId)).thenReturn(mockReview);

        // Act
        ResponseEntity<Review> response = reviewController.getReview(reviewId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockReview, response.getBody());
        verify(reviewService, times(1)).getReview(reviewId);
    }

    /**
     * Test the getReview endpoint when the review is not found.
     */
    @Test
    public void testGetReview_NotFound() {
        // Arrange
        String reviewId = "nonexistentReview";

        when(reviewService.getReview(reviewId))
                .thenThrow(new IllegalArgumentException("Review not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewController.getReview(reviewId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Review not found.", exception.getReason());
        verify(reviewService, times(1)).getReview(reviewId);
    }

    /**
     * Test the getAllReviewsForGame endpoint for a successful retrieval of reviews.
     */
    @Test
    public void testGetAllReviewsForGame_Success() {
        // Arrange
        String gameId = "game456";
        Review review1 = new Review("user123", gameId, "Great game!");
        Review review2 = new Review("user456", gameId, "Decent game.");
        List<Review> mockReviews = Arrays.asList(review1, review2);

        when(reviewService.getReviewsByGameId(gameId)).thenReturn(mockReviews);

        // Act
        ResponseEntity<List<Review>> response = reviewController.getAllReviewsForGame(gameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockReviews, response.getBody());
        verify(reviewService, times(1)).getReviewsByGameId(gameId);
    }

    /**
     * Test the getAllReviewsForGame endpoint when no reviews are found.
     */
    @Test
    public void testGetAllReviewsForGame_NoReviews() {
        // Arrange
        String gameId = "game456";

        when(reviewService.getReviewsByGameId(gameId))
                .thenThrow(new IllegalArgumentException("No reviews found for the given game ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewController.getAllReviewsForGame(gameId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No reviews found for the given game ID.", exception.getReason());
        verify(reviewService, times(1)).getReviewsByGameId(gameId);
    }

    /**
     * Test the deleteReview endpoint for a successful deletion of a review.
     */
    @Test
    public void testDeleteReview_Success() {
        // Arrange
        String reviewId = "review789";

        doNothing().when(reviewService).deleteReview(reviewId);

        // Act
        HttpStatus response = reviewController.deleteReview(reviewId);

        // Assert
        assertEquals(HttpStatus.OK, response);
        verify(reviewService, times(1)).deleteReview(reviewId);
    }

    /**
     * Test the deleteReview endpoint when the review does not exist.
     */
    @Test
    public void testDeleteReview_ReviewNotFound() {
        // Arrange
        String reviewId = "nonexistentReview";

        doThrow(new IllegalArgumentException("Review not found."))
                .when(reviewService).deleteReview(reviewId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewController.deleteReview(reviewId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Review not found.", exception.getReason());
        verify(reviewService, times(1)).deleteReview(reviewId);
    }
}
