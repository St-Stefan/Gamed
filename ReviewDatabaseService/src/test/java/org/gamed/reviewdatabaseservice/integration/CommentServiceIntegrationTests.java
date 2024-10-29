package org.gamed.reviewdatabaseservice.integration;

import org.gamed.reviewdatabaseservice.domain.Comment;
import org.gamed.reviewdatabaseservice.repository.CommentRepository;
import org.gamed.reviewdatabaseservice.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceIntegrationTests {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void testCreateCommentSuccess() {
        String userId = "user123";
        String parentId = "parent456";
        String description = "This is a comment";

        when(commentRepository.existsByUserIdAndParentId(userId, parentId)).thenReturn(false);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = commentService.createComment(userId, parentId, description);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(parentId, result.getParentId());
        assertEquals(description, result.getDescription());

        verify(commentRepository).existsByUserIdAndParentId(userId, parentId);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testCreateCommentThrowsExceptionWhenUserIdIsNull() {
        String userId = null;
        String parentId = "parent456";
        String description = "This is a comment";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.createComment(userId, parentId, description)
        );

        assertEquals("User ID cannot be blank.", exception.getMessage());

        verify(commentRepository, never()).existsByUserIdAndParentId(anyString(), anyString());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testCreateCommentThrowsExceptionWhenUserIdIsEmpty() {
        String userId = "";
        String parentId = "parent456";
        String description = "This is a comment";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.createComment(userId, parentId, description)
        );

        assertEquals("User ID cannot be blank.", exception.getMessage());

        verify(commentRepository, never()).existsByUserIdAndParentId(anyString(), anyString());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testCreateCommentThrowsExceptionWhenDescriptionIsNull() {
        String userId = "user123";
        String parentId = "parent456";
        String description = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.createComment(userId, parentId, description)
        );

        assertEquals("Description cannot be blank.", exception.getMessage());

        verify(commentRepository, never()).existsByUserIdAndParentId(anyString(), anyString());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testCreateCommentThrowsExceptionWhenDescriptionIsEmpty() {
        String userId = "user123";
        String parentId = "parent456";
        String description = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.createComment(userId, parentId, description)
        );

        assertEquals("Description cannot be blank.", exception.getMessage());

        verify(commentRepository, never()).existsByUserIdAndParentId(anyString(), anyString());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testCreateCommentThrowsExceptionWhenRelationAlreadyExists() {
        String userId = "user123";
        String parentId = "parent456";
        String description = "This is a comment";

        when(commentRepository.existsByUserIdAndParentId(userId, parentId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.createComment(userId, parentId, description)
        );

        assertEquals("Relation already exists.", exception.getMessage());

        verify(commentRepository).existsByUserIdAndParentId(userId, parentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testGetCommentThrowsExceptionWhenNotFound() {
        String id = "comment123";

        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.getComment(id)
        );

        assertEquals("Given Comment id does not correspond to any Comment. ID: " + id, exception.getMessage());

        verify(commentRepository).findById(id);
    }

    @Test
    void testUpdateCommentSuccess() {
        String id = "comment123";
        String newDescription = "Updated description";
        Comment existingComment = new Comment("user123", "parent456", "Old description");

        when(commentRepository.findById(id)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(existingComment);

        Comment result = commentService.updateComment(id, newDescription);

        assertNotNull(result);
        assertEquals(newDescription, result.getDescription());

        verify(commentRepository).findById(id);
        verify(commentRepository).save(existingComment);
    }

    @Test
    void testUpdateCommentThrowsExceptionWhenNotFound() {
        String id = "comment123";
        String newDescription = "Updated description";

        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.updateComment(id, newDescription)
        );

        assertEquals("Given Comment id does not correspond to any Comment. ID: " + id, exception.getMessage());

        verify(commentRepository).findById(id);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testUpdateCommentThrowsExceptionWhenDescriptionIsEmpty() {
        String id = "comment123";
        String newDescription = "";
        Comment existingComment = new Comment("user123", "parent456", "Old description");

        when(commentRepository.findById(id)).thenReturn(Optional.of(existingComment));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.updateComment(id, newDescription)
        );

        assertEquals("Description cannot be blank.", exception.getMessage());

        verify(commentRepository).findById(id);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testUpdateCommentWithNullDescription() {
        String id = "comment123";
        String newDescription = null;
        Comment existingComment = new Comment("user123", "parent456", "Old description");

        when(commentRepository.findById(id)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(existingComment);

        Comment result = commentService.updateComment(id, newDescription);

        assertNotNull(result);
        assertNull(result.getDescription());

        verify(commentRepository).findById(id);
        verify(commentRepository).save(existingComment);
    }

    @Test
    void testDeleteCommentSuccess() {
        String id = "comment123";

        when(commentRepository.existsById(id)).thenReturn(true);

        commentService.deleteComment(id);

        verify(commentRepository).existsById(id);
        verify(commentRepository).deleteById(id);
    }

    @Test
    void testDeleteCommentThrowsExceptionWhenNotFound() {
        String id = "comment123";

        when(commentRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commentService.deleteComment(id)
        );

        assertEquals("Given Comment id does not correspond to any Comment. ID: " + id, exception.getMessage());

        verify(commentRepository).existsById(id);
        verify(commentRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetCommentsByParentId() {
        String parentId = "parent456";
        List<Comment> comments = Arrays.asList(
                new Comment("user1", parentId, "Comment 1"),
                new Comment("user2", parentId, "Comment 2")
        );

        when(commentRepository.findByParentId(parentId)).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByParentId(parentId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(comments, result);

        verify(commentRepository).findByParentId(parentId);
    }
}
