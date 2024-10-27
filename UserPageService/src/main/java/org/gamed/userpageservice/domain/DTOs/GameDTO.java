package org.gamed.userpageservice.domain.DTOs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class GameDTO {

    private String id;

    private String name;

    private String developer;

    private LocalDateTime releaseDate;

    private String platforms;

    public GameDTO(String id, String name, String developer, LocalDateTime releaseDate, String platforms) {
        this.id = id;
        this.name = name;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.platforms = platforms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDTO gameDTO = (GameDTO) o;
        return id.equals(gameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", developer='" + developer + '\'' +
                ", release_date='" + releaseDate + '\'' +
                ", platforms='" + platforms + '\'' +
                '}';
    }
}