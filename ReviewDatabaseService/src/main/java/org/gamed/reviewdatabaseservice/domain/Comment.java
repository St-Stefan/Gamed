package org.gamed.reviewdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String user_id;

    @Column(nullable = false, updatable = false)
    private String parent_id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time_created;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime time_modified;

    public Comment() {}

    public Comment(String user_id, String parent_id, String description) {
        this.user_id = user_id;
        this.parent_id = parent_id;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public String getUserId() {
        return user_id;
    }

    public String getParentId() {
        return parent_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimeCreated() {
        return time_created;
    }

    public LocalDateTime getTimeModified() {
        return time_modified;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", description='" + description + '\'' +
                ", time_created=" + time_created +
                ", time_modified=" + time_modified +
                '}';
    }
}