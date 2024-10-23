package org.gamed.gamelistdatabaseservice.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "List_to_tag")
@EntityListeners(AuditingEntityListener.class)
public class ListToTag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String list_id;

    @Column(nullable = false, updatable = false)
    private String tag_id;

    public ListToTag() {}

    public ListToTag(String list_id, String tag_id) {
        this.list_id = list_id;
        this.tag_id = tag_id;
    }

    public String getId() {
        return id;
    }

    public String getList() {
        return list_id;
    }

    public String getTag() {
    return tag_id;
    }

    @Override
    public String toString() {
        return "ListToTag{" +
                "id='" + id + '\'' +
                ", list=" + list_id +
                ", tag=" + tag_id +
                '}';
    }
}