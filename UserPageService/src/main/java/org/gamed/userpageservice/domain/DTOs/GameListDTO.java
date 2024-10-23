package org.gamed.userpageservice.domain.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GameListDTO {
    private String id;

    private String userId;

    @Setter
    private String name;

    @Setter
    private String description;

    private LocalDateTime time_created;

    private LocalDateTime time_modified;

    public GameListDTO(String userId, String name, String description, LocalDateTime time_created, LocalDateTime time_modified) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.time_created = time_created;
        this.time_modified = time_modified;
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