package org.gamed.timelineservice.domain;

import java.time.LocalDateTime;

public class GameDTO {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public LocalDateTime getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDateTime release_date) {
        this.release_date = release_date;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    private String name;
    private String developer;
    private LocalDateTime release_date;
    private String platforms;

    public GameDTO() {}

    public GameDTO(String name, String developer, LocalDateTime release_date, String platforms) {
        this.name = name;
        this.developer = developer;
        this.release_date = release_date;
        this.platforms = platforms;
    }
}
