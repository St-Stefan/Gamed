package org.gamed.userpageservice.domain.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class GameListDTO {
    private String id;

    private String userId;

    @Setter
    private String name;

    @Setter
    private String description;

    @Setter
    private LocalDateTime time_created;

    @Setter
    private LocalDateTime time_modified;

    @Setter
    private List<GameDTO> games;

    public GameListDTO(String id, String userId, String name, String description, LocalDateTime time_created, LocalDateTime time_modified, List<GameDTO> games) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.time_created = time_created;
        this.time_modified = time_modified;
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
                ", time_created=" + time_created +
                ", time_modified=" + time_modified +
                '}';
    }
}