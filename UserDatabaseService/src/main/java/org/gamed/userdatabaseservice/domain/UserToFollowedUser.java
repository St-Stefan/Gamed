package org.gamed.userdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "User_to_followed_user")
@EntityListeners(AuditingEntityListener.class)
public class UserToFollowedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String user_id;

    @Column(nullable = false, updatable = false)
    private String followed_id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time;

    public UserToFollowedUser() {}

    public UserToFollowedUser(String user_id, String followed_id) {
        this.user_id = user_id;
        this.followed_id = followed_id;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user_id;
    }

    public String getFollowedId() {
        return followed_id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "UserToFollowedUser{" +
                "id='" + id + '\'' +
                ", user=" + user_id +
                ", followed_id='" + followed_id + '\'' +
                ", time=" + time +
                '}';
    }
}
