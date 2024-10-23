package org.gamed.reviewdatabaseservice.repository;

import org.gamed.reviewdatabaseservice.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, String> {

    @Query("SELECT r FROM Rating r WHERE r.id = :id")
    Rating getRatingById(@Param("id") String id);

    @Query("SELECT r FROM Rating r WHERE r.review.id = :reviewId")
    List<Rating> getRatingsByReviewId(@Param("reviewId") String reviewId);

    @Query("SELECT r FROM Rating r WHERE r.category = :category")
    List<Rating> getRatingsByCategory(@Param("category") String category);

    @Query("DELETE FROM Rating r WHERE r.id = :id")
    void deleteRatingById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rating r WHERE r.review.id = :reviewId AND r.category = :category")
    boolean existsByReviewIdAndCategory(@Param("reviewId") String reviewId, @Param("category") String category);
}
