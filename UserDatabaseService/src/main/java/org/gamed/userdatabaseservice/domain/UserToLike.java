package org.gamed.userdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "User_to_like")
@EntityListeners(AuditingEntityListener.class)
public class UserToLike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String user_id;

    @Column(nullable = false, updatable = false)
    private String item_id;

    @Column(nullable = false, updatable = false)
    private String type;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time;

    public UserToLike() {}

    public UserToLike(String user_id, String item_id, String type) {
        this.user_id = user_id;
        this.item_id = item_id;
        this.type = type;
        this.time = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user_id;
    }

    public String getItemId() {
        return item_id;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UserToLike{" +
                "id='" + id + '\'' +
                ", user=" + user_id +
                ", item_id='" + item_id + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
