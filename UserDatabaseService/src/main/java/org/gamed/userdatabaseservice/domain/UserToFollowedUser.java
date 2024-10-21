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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private String followed_id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time;

    public UserToFollowedUser() {}

    public UserToFollowedUser(User user, String followed_id) {
        this.user = user;
        this.followed_id = followed_id;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
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
                ", user=" + (user != null ? user.getId() : null) +
                ", followed_id='" + followed_id + '\'' +
                ", time=" + time +
                '}';
    }
}
