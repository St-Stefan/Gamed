package org.gamed.reviewdatabaseservice.controllers;

import org.gamed.reviewdatabaseservice.domain.Comment;
import org.gamed.reviewdatabaseservice.models.CreateAndUpdateCommentRequestModel;
import org.gamed.reviewdatabaseservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the Comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final transient CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Creates a new Comment and stores it in the database.
     *
     * @param request  the data the caller must provide in order to create a comment
     * @return the ID of the newly created comment
     */
    @PostMapping("/create")
    public ResponseEntity<String> createComment(@RequestBody CreateAndUpdateCommentRequestModel request) {
        try {
            Comment newComment = commentService.createComment(
                    request.getUserId(),
                    request.getParentId(),
                    request.getDescription()
            );
            return ResponseEntity.ok(newComment.getId());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to update comment information.
     *
     * @param commentId the ID of the comment to be updated
     * @param request  the data the caller must provide in order to update a comment
     * @return HTTP status OK if the operation succeeds
     */
    @PutMapping("/update/{commentId}")
    public HttpStatus updateComment(@PathVariable String commentId, @RequestBody CreateAndUpdateCommentRequestModel request) {
        try {
            commentService.updateComment(commentId, request.getDescription());
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get comment details by comment ID.
     *
     * @param commentId the ID of the comment
     * @return the Comment object if found
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable String commentId) {
        try {
            Comment comment = commentService.getComment(commentId);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get a list of all comments for a specific parent ID.
     *
     * @param parentId the ID of the parent entity (e.g., review or user)
     * @return a list of all Comment objects associated with the parent ID
     */
    @GetMapping("/all/{parentId}")
    public ResponseEntity<List<Comment>> getCommentsByParentId(@PathVariable String parentId) {
        try {
            List<Comment> comments = commentService.getCommentsByParentId(parentId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a comment by ID.
     *
     * @param commentId the ID of the comment to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{commentId}")
    public HttpStatus deleteComment(@PathVariable String commentId) {
        try {
            commentService.deleteComment(commentId);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
