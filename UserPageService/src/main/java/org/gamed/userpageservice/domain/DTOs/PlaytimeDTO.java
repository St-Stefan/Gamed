package org.gamed.userpageservice.domain.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaytimeDTO {
    private String user;

    private String id;

    private String gameId;

    @Setter
    private int playtime;

    private LocalDateTime time_created;

    private LocalDateTime time_modified;

    public PlaytimeDTO(String userId, String id, String gameId, int playtime) {
        this.user = userId;
        this.id = id;
        this.gameId = gameId;
        this.playtime = playtime;
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
                ", time_created=" + time_created +
                ", time_modified=" + time_modified +
                '}';
    }
}
