package org.gamed.reviewdatabaseservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class CommentTest {

    private Comment comment;

    @BeforeEach
    public void setUp() {
        comment = new Comment("user123", "parent456", "This is a comment.");
    }

    @Test
    public void testCommentCreation() {
        assertNotNull(comment, "Comment object should not be null");
        assertNull(comment.getId(), "ID should be null before persistence");
        assertEquals("user123", comment.getUserId(), "User ID should match the constructor argument");
        assertEquals("parent456", comment.getParentId(), "Parent ID should match the constructor argument");
        assertEquals("This is a comment.", comment.getDescription(), "Description should match the constructor argument");
        assertNull(comment.getTimeCreated(), "Creation time should be null before persistence");
        assertNull(comment.getTimeModified(), "Modification time should be null before persistence");
    }

    @Test
    public void testDefaultConstructor() {
        Comment defaultComment = new Comment();
        assertNotNull(defaultComment, "Default Comment object should not be null");
        assertNull(defaultComment.getId(), "ID should be null");
        assertNull(defaultComment.getUserId(), "User ID should be null");
        assertNull(defaultComment.getParentId(), "Parent ID should be null");
        assertNull(defaultComment.getDescription(), "Description should be null");
        assertNull(defaultComment.getTimeCreated(), "Creation time should be null");
        assertNull(defaultComment.getTimeModified(), "Modification time should be null");
    }

    @Test
    public void testSetDescription() {
        comment.setDescription("Updated comment.");
        assertEquals("Updated comment.", comment.getDescription(), "Description should be updated");
    }

    @Test
    public void testGetters() {
        assertEquals("user123", comment.getUserId(), "getUserId() should return the correct user ID");
        assertEquals("parent456", comment.getParentId(), "getParentId() should return the correct parent ID");
        assertEquals("This is a comment.", comment.getDescription(), "getDescription() should return the correct description");
    }

    @Test
    public void testToString() {
        String expected = "Comment{" +
                "id='" + comment.getId() + '\'' +
                ", user_id='" + comment.getUserId() + '\'' +
                ", parent_id='" + comment.getParentId() + '\'' +
                ", description='" + comment.getDescription() + '\'' +
                ", time_created=" + comment.getTimeCreated() +
                ", time_modified=" + comment.getTimeModified() +
                '}';
        assertEquals(expected, comment.toString(), "toString() should return the correct string representation");
    }
}
