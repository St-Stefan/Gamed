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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false, updatable = false)
    private GameList list;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false, updatable = false)
    private Tag tag;

    public ListToTag() {}

    public ListToTag(GameList list, Tag tag) {
        this.list = list;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public GameList getList() {
        return list;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "ListToTag{" +
                "id='" + id + '\'' +
                ", list=" + (list != null ? list.getId() : null) +
                ", tag=" + (tag != null ? tag.getId() : null) +
                '}';
    }
}