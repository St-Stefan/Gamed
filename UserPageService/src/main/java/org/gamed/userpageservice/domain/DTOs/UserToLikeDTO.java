package org.gamed.userpageservice.domain.DTOs;

import java.time.LocalDateTime;

public class UserToLikeDTO {

    private String id;

    private UserDTO userDTO;

    private String item_id;

    private String type;

    private LocalDateTime time;

    public UserToLikeDTO() {}

    public UserToLikeDTO(UserDTO userDTO, String item_id, String type) {
        this.userDTO = userDTO;
        this.item_id = item_id;
        this.type = type;
        this.time = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public String getItemId() {
        return item_id;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UserToLike{" +
                "id='" + id + '\'' +
                ", user=" + (userDTO != null ? userDTO.getId() : null) +
                ", item_id='" + item_id + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
