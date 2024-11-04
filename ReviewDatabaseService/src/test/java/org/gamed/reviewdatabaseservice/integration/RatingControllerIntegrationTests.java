package org.gamed.reviewdatabaseservice.integration;

import org.gamed.reviewdatabaseservice.controllers.RatingController;
import org.gamed.reviewdatabaseservice.domain.Rating;
import org.gamed.reviewdatabaseservice.domain.Review;
import org.gamed.reviewdatabaseservice.models.CreateAndUpdateRatingRequestModel;
import org.gamed.reviewdatabaseservice.service.RatingService;
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
 * Test class for RatingController.
 */
public class RatingControllerIntegrationTests {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createRating endpoint for a successful rating creation.
     */
    @Test
    public void testCreateRating_Success() {
        String reviewId = "review123";
        CreateAndUpdateRatingRequestModel request = new CreateAndUpdateRatingRequestModel(
                "Quality",
                5,
                "Graph data here"
        );

        Rating mockRating = new Rating(new Review(), "Quality", 5, "Graph data here");

        when(ratingService.createRating(
                reviewId,
                request.getCategory(),
                request.getScore(),
                request.getGraph()
        )).thenReturn(mockRating);

        ResponseEntity<String> response = ratingController.createRating(reviewId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(ratingService, times(1)).createRating(
                reviewId,
                request.getCategory(),
                request.getScore(),
                request.getGraph()
        );
    }

    /**
     * Test the createRating endpoint when an exception occurs.
     */
    @Test
    public void testCreateRating_Failure() {
        String reviewId = "review123";
        CreateAndUpdateRatingRequestModel request = new CreateAndUpdateRatingRequestModel(
                "Quality",
                5,
                "Graph data here"
        );

        when(ratingService.createRating(
                reviewId,
                request.getCategory(),
                request.getScore(),
                request.getGraph()
        )).thenThrow(new IllegalArgumentException("Invalid rating data."));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingController.createRating(reviewId, request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid rating data.", exception.getReason());
        verify(ratingService, times(1)).createRating(
                reviewId,
                request.getCategory(),
                request.getScore(),
                request.getGraph()
        );
    }

    /**
     * Test the updateRating endpoint for a successful rating update.
     */
    @Test
    public void testUpdateRating_Success() {
        // Arrange
        String ratingId = "rating456";
        CreateAndUpdateRatingRequestModel request = new CreateAndUpdateRatingRequestModel(
                "Gameplay",
                4,
                "Updated graph data"
        );

        when(ratingService.updateRating(
                ratingId,
                request.getCategory(),
                request.getScore(),
                request.getGraph()
        )).thenReturn(new Rating());

        // Act
        HttpStatus response = ratingController.updateRating(ratingId, request);

        // Assert
        assertEquals(HttpStatus.OK, response);
        verify(ratingService, times(1)).updateRating(
                ratingId,
                request.getCategory(),
                request.getScore(),
                request.getGraph()
        );
    }

    /**
     * Test the updateRating endpoint when the rating does not exist.
     */
    @Test
    public void testUpdateRating_RatingNotFound() {
        // Arrange
        String ratingId = "nonexistentRating";
        CreateAndUpdateRatingRequestModel request = new CreateAndUpdateRatingRequestModel(
                "Gameplay",
                4,
                "Updated graph data"
        );

        doThrow(new IllegalArgumentException("Rating not found."))
                .when(ratingService).updateRating(
                        ratingId,
                        request.getCategory(),
                        request.getScore(),
                        request.getGraph()
                );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingController.updateRating(ratingId, request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Rating not found.", exception.getReason());
        verify(ratingService, times(1)).updateRating(
                ratingId,
                request.getCategory(),
                request.getScore(),
                request.getGraph()
        );
    }

    /**
     * Test the getRating endpoint for a successful retrieval of a rating.
     */
    @Test
    public void testGetRating_Success() {
        // Arrange
        String ratingId = "rating456";
        Rating mockRating = new Rating(new Review(), "Quality", 5, "Graph data here");

        when(ratingService.getRating(ratingId)).thenReturn(mockRating);

        // Act
        ResponseEntity<Rating> response = ratingController.getRating(ratingId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRating, response.getBody());
        verify(ratingService, times(1)).getRating(ratingId);
    }

    /**
     * Test the getRating endpoint when the rating is not found.
     */
    @Test
    public void testGetRating_NotFound() {
        // Arrange
        String ratingId = "nonexistentRating";

        when(ratingService.getRating(ratingId))
                .thenThrow(new IllegalArgumentException("Rating not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingController.getRating(ratingId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Rating not found.", exception.getReason());
        verify(ratingService, times(1)).getRating(ratingId);
    }

    /**
     * Test the getRatingsByReviewId endpoint for a successful retrieval of ratings.
     */
    @Test
    public void testGetRatingsByReviewId_Success() {
        // Arrange
        String reviewId = "review123";
        Rating rating1 = new Rating(new Review(), "Quality", 5, "Graph data here");
        Rating rating2 = new Rating(new Review(), "Gameplay", 4, "More graph data");
        List<Rating> mockRatings = Arrays.asList(rating1, rating2);

        when(ratingService.getRatingsByReviewId(reviewId)).thenReturn(mockRatings);

        // Act
        ResponseEntity<List<Rating>> response = ratingController.getRatingsByReviewId(reviewId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRatings, response.getBody());
        verify(ratingService, times(1)).getRatingsByReviewId(reviewId);
    }

    /**
     * Test the getRatingsByReviewId endpoint when no ratings are found.
     */
    @Test
    public void testGetRatingsByReviewId_NoRatings() {
        // Arrange
        String reviewId = "review123";

        when(ratingService.getRatingsByReviewId(reviewId))
                .thenThrow(new IllegalArgumentException("No ratings found for the given review ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingController.getRatingsByReviewId(reviewId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No ratings found for the given review ID.", exception.getReason());
        verify(ratingService, times(1)).getRatingsByReviewId(reviewId);
    }

    /**
     * Test the deleteRating endpoint for a successful deletion of a rating.
     */
    @Test
    public void testDeleteRating_Success() {
        // Arrange
        String ratingId = "rating456";

        doNothing().when(ratingService).deleteRating(ratingId);

        // Act
        HttpStatus response = ratingController.deleteRating(ratingId);

        // Assert
        assertEquals(HttpStatus.OK, response);
        verify(ratingService, times(1)).deleteRating(ratingId);
    }

    /**
     * Test the deleteRating endpoint when the rating does not exist.
     */
    @Test
    public void testDeleteRating_RatingNotFound() {
        // Arrange
        String ratingId = "nonexistentRating";

        doThrow(new IllegalArgumentException("Rating not found."))
                .when(ratingService).deleteRating(ratingId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingController.deleteRating(ratingId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Rating not found.", exception.getReason());
        verify(ratingService, times(1)).deleteRating(ratingId);
    }
}
