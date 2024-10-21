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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false, updatable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false, updatable = false)
    private Tag tag;

    @Formula("(SELECT COUNT(*) FROM Game_to_tag gt WHERE gt.game_id = game_id AND gt.tag_id = tag_id)")
    private int occurrences;

    public GameToTag() {}

    public GameToTag(Game game, Tag tag) {
        this.game = game;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Tag getTag() {
        return tag;
    }

    public int getOccurrences() {
        return occurrences;
    }

    @Override
    public String toString() {
        return "GameToTag{" +
                "id='" + id + '\'' +
                ", game=" + (game != null ? game.getId() : null) +
                ", tag=" + (tag != null ? tag.getId() : null) +
                ", occurrences=" + occurrences +
                '}';
    }
}