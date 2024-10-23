package org.gamed.userpageservice.domain.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaytimeDTO {
//    private String user;

    private String id;

    private String gameId;

    @Setter
    private int playtime;

    private LocalDateTime time_created;

    private LocalDateTime time_modified;

    public PlaytimeDTO(String id, String gameId, int playtime) {
//        this.user = userId;
        this.id = id;
        this.gameId = gameId;
        this.playtime = playtime;
    }

    @Override
    public String toString() {
        return "UserToPlaytime{" +
                "id='" + id + '\'' +
//                ", user=" + user +
                ", game_id='" + gameId + '\'' +
                ", playtime=" + playtime +
                ", time_created=" + time_created +
                ", time_modified=" + time_modified +
                '}';
    }
}
