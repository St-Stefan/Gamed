package org.gamed.gamelistdatabaseservice.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "List_to_game")
@EntityListeners(AuditingEntityListener.class)
public class ListToGame {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false, updatable = false)
    private GameList list;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false, updatable = false)
    private Game game;

    public ListToGame() {}

    public ListToGame(GameList list, Game game) {
        this.list = list;
        this.game = game;
    }

    public String getId() {
        return id;
    }

    public GameList getList() {
        return list;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "ListToGame{" +
                "id='" + id + '\'' +
                ", list=" + (list != null ? list.getId() : null) +
                ", game=" + (game != null ? game.getId() : null) +
                '}';
    }
}