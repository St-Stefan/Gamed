package org.gamed.gamelistdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "GameList")
@EntityListeners(AuditingEntityListener.class)
public class GameList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String user_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time_created;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime time_modified;

    public GameList() {}

    public GameList(String user_id, String name, String description) {
        this.user_id = user_id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "List{" +
                "id='" + id + '\'' +
                ", user='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", time_created=" + time_created +
                ", time_modified=" + time_modified +
                '}';
    }
}