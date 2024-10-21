package org.gamed.userpageservice.domain.DTOs;

import java.time.LocalDateTime;


public class UserToFollowedListDTO {
    private String id;

    private UserDTO userDTO;

    private String list_id;

    private LocalDateTime time;

    public UserToFollowedListDTO() {}

    public UserToFollowedListDTO(UserDTO userDTO, String list_id) {
        this.userDTO = userDTO;
        this.list_id = list_id;
        this.time = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public String getListId() {
        return list_id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "UserToFollowedList{" +
                "id='" + id + '\'' +
                ", user=" + (userDTO != null ? userDTO.getId() : null) +
                ", list_id='" + list_id + '\'' +
                ", time=" + time +
                '}';
    }
}
