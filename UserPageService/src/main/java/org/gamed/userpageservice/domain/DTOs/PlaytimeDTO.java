package org.gamed.userpageservice.domain.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaytimeDTO {
    private String user;

    private String id;

    private String gameId;

    private int playtime;

    private LocalDateTime timeCreated;

    private LocalDateTime timeModified;

    public PlaytimeDTO(String userId, String id, String gameId, int playtime, LocalDateTime timeCreated, LocalDateTime timeModified) {
        this.user = userId;
        this.id = id;
        this.gameId = gameId;
        this.playtime = playtime;
        this.timeCreated = timeCreated;
        this.timeModified = timeModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaytimeDTO that = (PlaytimeDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserToPlaytime{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", game_id='" + gameId + '\'' +
                ", playtime=" + playtime +
                ", time_created=" + timeCreated +
                ", time_modified=" + timeModified +
                '}';
    }
}
