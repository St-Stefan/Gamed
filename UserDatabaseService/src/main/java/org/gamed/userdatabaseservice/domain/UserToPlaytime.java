package org.gamed.userdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "User_to_playtime")
@EntityListeners(AuditingEntityListener.class)
public class UserToPlaytime {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String user_id;

    @Column(nullable = false, updatable = false)
    private String game_id;

    @Column(nullable = false)
    private int playtime;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time_created;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime time_modified;

    public UserToPlaytime() {}

    public UserToPlaytime(String user_id, String game_id, int playtime) {
        this.user_id = user_id;
        this.game_id = game_id;
        this.playtime = playtime;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user_id;
    }

    public String getGameId() {
        return game_id;
    }

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public LocalDateTime getTimeCreated() {
        return time_created;
    }

    public LocalDateTime getTimeModified() {
        return time_modified;
    }

    @Override
    public String toString() {
        return "UserToPlaytime{" +
                "id='" + id + '\'' +
                ", user=" + user_id +
                ", game_id='" + game_id + '\'' +
                ", playtime=" + playtime +
                ", time_created=" + time_created +
                ", time_modified=" + time_modified +
                '}';
    }
}
