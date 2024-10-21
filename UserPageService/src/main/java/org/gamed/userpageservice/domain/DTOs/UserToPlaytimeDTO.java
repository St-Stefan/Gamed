package org.gamed.userpageservice.domain.DTOs;

import java.time.LocalDateTime;

public class UserToPlaytimeDTO {

    private String id;

    private UserDTO userDTO;

    private String game_id;

    private int playtime;

    private LocalDateTime time_created;

    private LocalDateTime time_modified;

    public UserToPlaytimeDTO() {}

    public UserToPlaytimeDTO(UserDTO userDTO, String game_id, int playtime) {
        this.userDTO = userDTO;
        this.game_id = game_id;
        this.playtime = playtime;
    }

    public String getId() {
        return id;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public String getGameId() {
        return game_id;
    }

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public LocalDateTime getTimeCreated() {
        return time_created;
    }

    public LocalDateTime getTimeModified() {
        return time_modified;
    }

    @Override
    public String toString() {
        return "UserToPlaytime{" +
                "id='" + id + '\'' +
                ", user=" + (userDTO != null ? userDTO.getId() : null) +
                ", game_id='" + game_id + '\'' +
                ", playtime=" + playtime +
                ", time_created=" + time_created +
                ", time_modified=" + time_modified +
                '}';
    }
}
