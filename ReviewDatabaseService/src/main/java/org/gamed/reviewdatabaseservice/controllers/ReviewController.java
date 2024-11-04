package org.gamed.reviewdatabaseservice.controllers;

import org.gamed.reviewdatabaseservice.domain.Review;
import org.gamed.reviewdatabaseservice.models.CreateAndUpdateReviewRequestModel;
import org.gamed.reviewdatabaseservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the Review entity.
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Creates a new Review and stores it in the database.
     *
     * @param request  the data the caller must provide to create a review
     * @return the ID of the newly created review
     */
    @PostMapping("/create")
    public ResponseEntity<String> createReview(@RequestBody CreateAndUpdateReviewRequestModel request) {
        try {
            Review newReview = reviewService.createReview(
                    request.getUserId(),
                    request.getGameId(),
                    request.getDescription()
            );
            return ResponseEntity.ok(newReview.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to update review information.
     *
     * @param reviewId the ID of the review to be updated
     * @param request  the data the caller must provide to update a review
     * @return HTTP status OK if the operation succeeds
     */
    @PutMapping("/update/{reviewId}")
    public HttpStatus updateReview(@PathVariable String reviewId, @RequestBody CreateAndUpdateReviewRequestModel request) {
        try {
            reviewService.updateReview(reviewId, request.getDescription());
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get review details by review ID.
     *
     * @param reviewId the ID of the review
     * @return the Review object if found
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable String reviewId) {
        try {
            Review review = reviewService.getReview(reviewId);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get all reviews for a specific game by game ID.
     *
     * @param gameId the ID of the game
     * @return a list of all reviews for the given game
     */
    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<Review>> getAllReviewsForGame(@PathVariable String gameId) {
        try {
            List<Review> reviews = reviewService.getReviewsByGameId(gameId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getAllReviewsForUser(@PathVariable String userId) {
        try {
            List<Review> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a review by ID.
     *
     * @param reviewId the ID of the review to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{reviewId}")
    public HttpStatus deleteReview(@PathVariable String reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
