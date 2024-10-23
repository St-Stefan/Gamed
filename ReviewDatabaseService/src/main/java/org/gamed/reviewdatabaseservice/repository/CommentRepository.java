package org.gamed.reviewdatabaseservice.repository;

import org.gamed.reviewdatabaseservice.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query("SELECT c FROM Comment c WHERE c.id = :id")
    Comment getCommentById(@Param("id") String id);

    @Query("SELECT c FROM Comment c WHERE c.parent_id = :parentId")
    List<Comment> findByParentId(@Param("parentId") String parentId);

    @Query("SELECT c FROM Comment c WHERE c.user_id = :userId")
    List<Comment> getCommentsByUserId(@Param("userId") String userId);

    @Query("SELECT c FROM Comment c WHERE c.parent_id = :parentId")
    List<Comment> getCommentsByParentId(@Param("parentId") String parentId);

    @Query("DELETE FROM Comment c WHERE c.id = :id")
    void deleteCommentById(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Comment c WHERE c.user_id = :userId AND c.parent_id = :parentId")
    boolean existsByUserIdAndParentId(@Param("userId") String userId, @Param("parentId") String parentId);
}
