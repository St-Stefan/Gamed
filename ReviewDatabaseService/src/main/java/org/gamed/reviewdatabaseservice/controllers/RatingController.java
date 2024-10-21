package org.gamed.reviewdatabaseservice.controllers;

import org.gamed.reviewdatabaseservice.domain.Rating;
import org.gamed.reviewdatabaseservice.models.CreateAndUpdateRatingRequestModel;
import org.gamed.reviewdatabaseservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the Rating entity.
 */
@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Creates a new Rating and stores it in the database.
     *
     * @param request  the data the caller must provide to create a rating
     * @return the ID of the newly created rating
     */
    @PostMapping("/create/{reviewId}")
    public ResponseEntity<String> createRating(@PathVariable String reviewId, @RequestBody CreateAndUpdateRatingRequestModel request) {
        try {
            Rating newRating = ratingService.createRating(
                    reviewId,
                    request.getCategory(),
                    request.getScore(),
                    request.getGraph()
            );
            return ResponseEntity.ok(newRating.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to update rating information.
     *
     * @param ratingId the ID of the rating to be updated
     * @param request  the data the caller must provide to update a rating
     * @return HTTP status OK if the operation succeeds
     */
    @PutMapping("/update/{ratingId}")
    public HttpStatus updateRating(@PathVariable String ratingId, @RequestBody CreateAndUpdateRatingRequestModel request) {
        try {
            ratingService.updateRating(ratingId, request.getCategory(), request.getScore(), request.getGraph());
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get rating details by rating ID.
     *
     * @param ratingId the ID of the rating
     * @return the Rating object if found
     */
    @GetMapping("/{ratingId}")
    public ResponseEntity<Rating> getRating(@PathVariable String ratingId) {
        try {
            Rating rating = ratingService.getRating(ratingId);
            return ResponseEntity.ok(rating);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get all ratings for a specific review.
     *
     * @param reviewId the ID of the review
     * @return a list of ratings for the review
     */
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<Rating>> getRatingsByReviewId(@PathVariable String reviewId) {
        try {
            List<Rating> ratings = ratingService.getRatingsByReviewId(reviewId);
            return ResponseEntity.ok(ratings);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a rating by ID.
     *
     * @param ratingId the ID of the rating to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{ratingId}")
    public HttpStatus deleteRating(@PathVariable String ratingId) {
        try {
            ratingService.deleteRating(ratingId);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
