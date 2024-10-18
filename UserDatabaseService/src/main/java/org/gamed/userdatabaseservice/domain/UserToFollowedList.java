package org.gamed.userdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "User_to_followed_list")
@EntityListeners(AuditingEntityListener.class)
public class UserToFollowedList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private String list_id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time;

    public UserToFollowedList() {}

    public UserToFollowedList(User user, String list_id) {
        this.user = user;
        this.list_id = list_id;
        this.time = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getListId() {
        return list_id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "UserToFollowedList{" +
                "id='" + id + '\'' +
                ", user=" + (user != null ? user.getId() : null) +
                ", list_id='" + list_id + '\'' +
                ", time=" + time +
                '}';
    }
}
