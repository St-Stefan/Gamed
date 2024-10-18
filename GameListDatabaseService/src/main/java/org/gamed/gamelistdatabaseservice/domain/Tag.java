package org.gamed.gamelistdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Tag")
@EntityListeners(AuditingEntityListener.class)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private int time_created;

    public Tag() {}

    public Tag(String name, int time_created) {
        this.name = name;
        this.time_created = time_created;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTimeCreated() {
        return time_created;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", time_created=" + time_created +
                '}';
    }
}