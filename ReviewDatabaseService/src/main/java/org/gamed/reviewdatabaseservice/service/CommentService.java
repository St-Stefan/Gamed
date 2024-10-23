package org.gamed.reviewdatabaseservice.service;

import org.gamed.reviewdatabaseservice.domain.Comment;
import org.gamed.reviewdatabaseservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Creates a new Comment and stores it in the database.
     *
     * @param userId      the ID of the user making the comment
     * @param parentId    the ID of the parent comment or review (if any)
     * @param description the description of the comment
     * @return the newly created Comment object
     * @throws IllegalArgumentException if the userId or description is blank, or parentId is invalid, or if the relation already exists.
     */
    public Comment createComment(String userId, String parentId, String description)
            throws IllegalArgumentException {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be blank.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be blank.");
        }

        if(commentRepository.existsByUserIdAndParentId(userId, parentId)) {
            throw new IllegalArgumentException("Relation already exists.");
        }

        Comment comment = new Comment(userId, parentId, description);
        return commentRepository.save(comment);
    }

    /**
     * Gets the Comment by id from the database.
     *
     * @param id The id of the Comment to get
     * @return the Comment mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any Comment
     */
    public Comment getComment(String id) throws IllegalArgumentException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new IllegalArgumentException("Given Comment id does not correspond to any Comment. ID: " + id);
        }
        return comment.get();
    }

    /**
     * Updates the comment's description.
     *
     * @param id          The id of the comment to update
     * @param description The new description for the comment
     * @return the updated Comment object
     * @throws IllegalArgumentException if the comment is not found or the description is blank.
     */
    public Comment updateComment(String id, String description) throws IllegalArgumentException {
        Comment comment = getComment(id);

        if (description != null && description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be blank.");
        }

        comment.setDescription(description);
        return commentRepository.save(comment);
    }

    /**
     * Deletes a Comment by id from the database.
     *
     * @param id The id of the Comment to delete
     * @throws IllegalArgumentException when the given id does not map to any Comment
     */
    public void deleteComment(String id) throws IllegalArgumentException {
        if (!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("Given Comment id does not correspond to any Comment. ID: " + id);
        }
        commentRepository.deleteById(id);
    }

    /**
     * Retrieves all comments associated with a given parent ID.
     *
     * @param parentId the ID of the parent entity (e.g., review or user)
     * @return a list of comments associated with the parent entity
     */
    public List<Comment> getCommentsByParentId(String parentId) {
        return commentRepository.findByParentId(parentId);
    }
}
