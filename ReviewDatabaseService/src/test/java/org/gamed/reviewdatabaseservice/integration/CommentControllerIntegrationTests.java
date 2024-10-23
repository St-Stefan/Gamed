package org.gamed.reviewdatabaseservice.integration;

import org.gamed.reviewdatabaseservice.controllers.CommentController;
import org.gamed.reviewdatabaseservice.domain.Comment;
import org.gamed.reviewdatabaseservice.models.CreateAndUpdateCommentRequestModel;
import org.gamed.reviewdatabaseservice.service.CommentService;
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
 * Test class for CommentController.
 */
public class CommentControllerIntegrationTests {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the createComment endpoint for a successful comment creation.
     */
    @Test
    public void testCreateComment_Success() {
        // Arrange
        CreateAndUpdateCommentRequestModel request = new CreateAndUpdateCommentRequestModel(
                "user123",
                "parent456",
                "This is a test comment."
        );

        Comment mockComment = new Comment("user123", "parent456", "This is a test comment.");

        when(commentService.createComment(
                request.getUserId(),
                request.getParentId(),
                request.getDescription()
        )).thenReturn(mockComment);

        // Act
        ResponseEntity<String> response = commentController.createComment(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(commentService, times(1)).createComment(
                request.getUserId(),
                request.getParentId(),
                request.getDescription()
        );
    }

    /**
     * Test the createComment endpoint when an exception occurs.
     */
    @Test
    public void testCreateComment_Failure() {
        // Arrange
        CreateAndUpdateCommentRequestModel request = new CreateAndUpdateCommentRequestModel(
                "user123",
                "parent456",
                "This is a test comment."
        );

        when(commentService.createComment(
                request.getUserId(),
                request.getParentId(),
                request.getDescription()
        )).thenThrow(new IllegalArgumentException("Invalid input data."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            commentController.createComment(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid input data.", exception.getReason());
        verify(commentService, times(1)).createComment(
                request.getUserId(),
                request.getParentId(),
                request.getDescription()
        );
    }

    /**
     * Test the updateComment endpoint for a successful comment update.
     */
    @Test
    public void testUpdateComment_Success() {
        // Arrange
        String commentId = "comment789";
        CreateAndUpdateCommentRequestModel request = new CreateAndUpdateCommentRequestModel(
                "user123",
                "parent456",
                "Updated comment description."
        );

        when(commentService.updateComment(commentId, request.getDescription())).thenReturn(new Comment());

        // Act
        HttpStatus response = commentController.updateComment(commentId, request);

        // Assert
        assertEquals(HttpStatus.OK, response);
        verify(commentService, times(1)).updateComment(commentId, request.getDescription());
    }

    /**
     * Test the updateComment endpoint when the comment does not exist.
     */
    @Test
    public void testUpdateComment_CommentNotFound() {
        // Arrange
        String commentId = "nonexistentComment";
        CreateAndUpdateCommentRequestModel request = new CreateAndUpdateCommentRequestModel(
                "user123",
                "parent456",
                "Updated comment description."
        );

        doThrow(new IllegalArgumentException("Comment not found."))
                .when(commentService).updateComment(commentId, request.getDescription());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            commentController.updateComment(commentId, request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Comment not found.", exception.getReason());
        verify(commentService, times(1)).updateComment(commentId, request.getDescription());
    }

    /**
     * Test the getComment endpoint for a successful retrieval of a comment.
     */
    @Test
    public void testGetComment_Success() {
        // Arrange
        String commentId = "comment789";
        Comment mockComment = new Comment("user123", "parent456", "This is a test comment.");

        when(commentService.getComment(commentId)).thenReturn(mockComment);

        // Act
        ResponseEntity<Comment> response = commentController.getComment(commentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockComment, response.getBody());
        verify(commentService, times(1)).getComment(commentId);
    }

    /**
     * Test the getComment endpoint when the comment is not found.
     */
    @Test
    public void testGetComment_NotFound() {
        // Arrange
        String commentId = "nonexistentComment";

        when(commentService.getComment(commentId))
                .thenThrow(new IllegalArgumentException("Comment not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            commentController.getComment(commentId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Comment not found.", exception.getReason());
        verify(commentService, times(1)).getComment(commentId);
    }

    /**
     * Test the getCommentsByParentId endpoint for a successful retrieval of comments.
     */
    @Test
    public void testGetCommentsByParentId_Success() {
        // Arrange
        String parentId = "parent456";
        Comment comment1 = new Comment("user123", "parent456", "First comment.");
        Comment comment2 = new Comment("user456", "parent456", "Second comment.");
        List<Comment> mockComments = Arrays.asList(comment1, comment2);

        when(commentService.getCommentsByParentId(parentId)).thenReturn(mockComments);

        // Act
        ResponseEntity<List<Comment>> response = commentController.getCommentsByParentId(parentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockComments, response.getBody());
        verify(commentService, times(1)).getCommentsByParentId(parentId);
    }

    /**
     * Test the getCommentsByParentId endpoint when no comments are found.
     */
    @Test
    public void testGetCommentsByParentId_NoComments() {
        // Arrange
        String parentId = "parent456";

        when(commentService.getCommentsByParentId(parentId))
                .thenThrow(new IllegalArgumentException("No comments found for the given parent ID."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            commentController.getCommentsByParentId(parentId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No comments found for the given parent ID.", exception.getReason());
        verify(commentService, times(1)).getCommentsByParentId(parentId);
    }

    /**
     * Test the deleteComment endpoint for a successful comment deletion.
     */
    @Test
    public void testDeleteComment_Success() {
        // Arrange
        String commentId = "comment789";

        doNothing().when(commentService).deleteComment(commentId);

        // Act
        HttpStatus response = commentController.deleteComment(commentId);

        // Assert
        assertEquals(HttpStatus.OK, response);
        verify(commentService, times(1)).deleteComment(commentId);
    }

    /**
     * Test the deleteComment endpoint when the comment does not exist.
     */
    @Test
    public void testDeleteComment_CommentNotFound() {
        // Arrange
        String commentId = "nonexistentComment";

        doThrow(new IllegalArgumentException("Comment not found."))
                .when(commentService).deleteComment(commentId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            commentController.deleteComment(commentId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Comment not found.", exception.getReason());
        verify(commentService, times(1)).deleteComment(commentId);
    }
}
