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

    @Column(nullable = false, updatable = false)
    private String user_id;

    @Column(nullable = false, updatable = false)
    private String list_id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time;

    public UserToFollowedList() {}

    public UserToFollowedList(String user_id, String list_id) {
        this.user_id = user_id;
        this.list_id = list_id;
        this.time = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user_id;
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
                ", user=" + user_id +
                ", list_id='" + list_id + '\'' +
                ", time=" + time +
                '}';
    }
}
