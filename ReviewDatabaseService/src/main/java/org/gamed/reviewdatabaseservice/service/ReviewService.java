package org.gamed.reviewdatabaseservice.service;

import org.gamed.reviewdatabaseservice.domain.Review;
import org.gamed.reviewdatabaseservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Creates a new Review and stores it in the database.
     *
     * @param userId       the id of the User who created the review
     * @param gameId       the id of the Game being reviewed
     * @param description the description of the review
     * @return the newly created Review object
     * @throws IllegalArgumentException if the description is blank.
     */
    public Review createReview(String userId, String gameId, String description)
            throws IllegalArgumentException {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be blank.");
        }

        Review review = new Review(userId, gameId, description);
        return reviewRepository.save(review);
    }

    /**
     * Gets the Review by id from the database.
     *
     * @param id The id of the Review to get
     * @return the Review mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any Review
     */
    public Review getReview(String id) throws IllegalArgumentException {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            throw new IllegalArgumentException("Given Review id does not correspond to any Review. ID: " + id);
        }
        return review.get();
    }

    /**
     * Updates the review's description.
     *
     * @param id          The id of the review to update
     * @param description The new description for the review
     * @return the updated Review object
     * @throws IllegalArgumentException if the review is not found or the description is blank.
     */
    public Review updateReview(String id, String description) throws IllegalArgumentException {
        Review review = getReview(id);

        if (description != null && description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be blank.");
        }

        review.setDescription(description);
        return reviewRepository.save(review);
    }

    /**
     * Deletes a Review by id from the database.
     *
     * @param id The id of the Review to delete
     * @throws IllegalArgumentException when the given id does not map to any Review
     */
    public void deleteReview(String id) throws IllegalArgumentException {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Given Review id does not correspond to any Review. ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    /**
     * Retrieves all reviews for a specific game by game ID.
     *
     * @param gameId the ID of the game
     * @return a list of reviews associated with the given game ID
     */
    public List<Review> getReviewsByGameId(String gameId) {
        return reviewRepository.getReviewsByGameId(gameId);
    }
}
