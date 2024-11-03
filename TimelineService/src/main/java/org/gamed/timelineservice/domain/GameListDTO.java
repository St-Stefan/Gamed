package org.gamed.timelineservice.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


public class GameListDTO {
    private String id;

    private String userId;

    private String name;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime_created() {
        return time_created;
    }

    public void setTime_created(LocalDateTime time_created) {
        this.time_created = time_created;
    }

    public LocalDateTime getTime_modified() {
        return time_modified;
    }

    public void setTime_modified(LocalDateTime time_modified) {
        this.time_modified = time_modified;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    private LocalDateTime time_created;

    private LocalDateTime time_modified;

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

    public GameListDTO() {
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
