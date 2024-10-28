package org.gamed.userpageservice.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class GameListDTO {
    private String id;

    private String userId;

    private String name;

    private String description;

    private LocalDateTime timeCreated;

    private LocalDateTime timeModified;

    private List<GameDTO> games;

    public GameListDTO(String id, String userId, String name, String description, LocalDateTime time_created, LocalDateTime time_modified, List<GameDTO> games) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.timeCreated = time_created;
        this.timeModified = time_modified;
        this.games = games;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameListDTO that = (GameListDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "List{" +
                "id='" + id + '\'' +
                ", user=" + userId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", time_created=" + timeCreated +
                ", time_modified=" + timeModified +
                '}';
    }
}