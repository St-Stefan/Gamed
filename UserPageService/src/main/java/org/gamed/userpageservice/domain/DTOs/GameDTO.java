package org.gamed.userpageservice.domain.DTOs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GameDTO {

    private String id;

    private String name;

    private String developer;

    @Setter
    private LocalDateTime releaseDate;

    @Setter
    private String platforms;

    public GameDTO(String name, String developer, LocalDateTime releaseDate, String platforms) {
        this.name = name;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.platforms = platforms;
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