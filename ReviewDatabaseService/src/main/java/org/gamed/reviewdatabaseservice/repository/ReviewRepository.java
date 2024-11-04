package org.gamed.reviewdatabaseservice.repository;

import org.gamed.reviewdatabaseservice.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {

    @Query("SELECT r FROM Review r WHERE r.id = :id")
    Review getReviewById(@Param("id") String id);

    @Query("SELECT r FROM Review r WHERE r.user_id = :userId")
    List<Review> getReviewsByUserId(@Param("userId") String userId);

    @Query("SELECT r FROM Review r WHERE r.game_id = :gameId")
    List<Review> getReviewsByGameId(@Param("gameId") String gameId);

    @Query("DELETE FROM Review r WHERE r.id = :id")
    void deleteReviewById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Review r WHERE r.user_id = :userId AND r.game_id = :gameId")
    boolean existsByUserIdAndGameId(@Param("userId") String userId, @Param("gameId") String gameId);
}
