package org.gamed.gamelistdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Game_to_tag")
@EntityListeners(AuditingEntityListener.class)
public class GameToTag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String game_id;

    @Column(nullable = false, updatable = false)
    private String tag_id;

    @Formula("(SELECT COUNT(*) FROM Game_to_tag gt WHERE gt.game_id = game_id AND gt.tag_id = tag_id)")
    private int occurrences;

    public GameToTag() {}

    public GameToTag(String game_id, String tag_id) {
        this.game_id = game_id;
        this.tag_id = tag_id;
    }

    public String getId() {
        return id;
    }

    public String getGame() {
        return game_id;
    }

    public String getTag() {
        return tag_id;
    }

    public int getOccurrences() {
        return occurrences;
    }

    @Override
    public String toString() {
        return "GameToTag{" +
                "id='" + id + '\'' +
                ", game=" + game_id +
                ", tag=" + tag_id +
                ", occurrences=" + occurrences +
                '}';
    }
}