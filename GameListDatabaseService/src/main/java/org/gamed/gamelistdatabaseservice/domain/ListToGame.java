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

    @Column(nullable = false, updatable = false)
    private String list_id;

    @Column(nullable = false, updatable = false)
    private String game_id;

    public ListToGame() {}

    public ListToGame(String list_id, String game_id) {
        this.list_id = list_id;
        this.game_id = game_id;
    }

    public String getId() {
        return id;
    }

    public String getList() {
        return list_id;
    }

    public String  getGame() {
        return game_id;
    }

    @Override
    public String toString() {
        return "ListToGame{" +
                "id='" + id + '\'' +
                ", list=" + list_id +
                ", game=" + game_id +
                '}';
    }
}