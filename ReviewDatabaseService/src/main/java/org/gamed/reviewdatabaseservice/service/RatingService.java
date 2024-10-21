package org.gamed.reviewdatabaseservice.service;

import org.gamed.reviewdatabaseservice.domain.Rating;
import org.gamed.reviewdatabaseservice.repository.RatingRepository;
import org.gamed.reviewdatabaseservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, ReviewRepository reviewRepository) {
        this.ratingRepository = ratingRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Creates a new Rating and stores it in the database.
     *
     * @param reviewId   the ID of the Review associated with the rating
     * @param category    the category of the rating
     * @param score      the score given in the rating
     * @param graph      the graph representation of the rating
     * @return the newly created Rating object
     * @throws IllegalArgumentException if the category is blank or the score is out of range.
     */
    public Rating createRating(String reviewId, String category, int score, String graph)
            throws IllegalArgumentException {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be blank.");
        }
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Score must be between 0 and 10.");
        }

        Rating rating = new Rating(reviewRepository.getReviewById(reviewId), category, score, graph);
        return ratingRepository.save(rating);
    }

    /**
     * Gets the Rating by id from the database.
     *
     * @param id The id of the Rating to get
     * @return the Rating mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any Rating
     */
    public Rating getRating(String id) throws IllegalArgumentException {
        Optional<Rating> rating = ratingRepository.findById(id);
        if (rating.isEmpty()) {
            throw new IllegalArgumentException("Given Rating id does not correspond to any Rating. ID: " + id);
        }
        return rating.get();
    }

    /**
     * Updates the rating's score and category.
     *
     * @param id        The id of the rating to update
     * @param category  The new category for the rating
     * @param score     The new score for the rating
     * @return the updated Rating object
     * @throws IllegalArgumentException if the rating is not found or the category is blank or the score is out of range.
     */
    public Rating updateRating(String id, String category, int score, String graph) throws IllegalArgumentException {
        Rating rating = getRating(id);

        if (category != null && category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be blank.");
        }
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Score must be between 0 and 10.");
        }

        rating.setCategory(category);
        rating.setScore(score);
        rating.setGraph(graph);
        return ratingRepository.save(rating);
    }

    /**
     * Deletes a Rating by id from the database.
     *
     * @param id The id of the Rating to delete
     * @throws IllegalArgumentException when the given id does not map to any Rating
     */
    public void deleteRating(String id) throws IllegalArgumentException {
        if (!ratingRepository.existsById(id)) {
            throw new IllegalArgumentException("Given Rating id does not correspond to any Rating. ID: " + id);
        }
        ratingRepository.deleteById(id);
    }

    /**
     * Gets all ratings for a specific review.
     *
     * @param reviewId the ID of the review
     * @return a list of ratings
     */
    public List<Rating> getRatingsByReviewId(String reviewId) {
        return ratingRepository.getRatingsByReviewId(reviewId);
    }
}
