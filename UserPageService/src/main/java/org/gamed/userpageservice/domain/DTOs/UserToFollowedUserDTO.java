package org.gamed.userpageservice.domain.DTOs;

import java.time.LocalDateTime;

public class UserToFollowedUserDTO {

    private String id;

    private UserDTO userDTO;

    private String followed_id;

    private LocalDateTime time;

    public UserToFollowedUserDTO() {}

    public UserToFollowedUserDTO(UserDTO userDTO, String followed_id) {
        this.userDTO = userDTO;
        this.followed_id = followed_id;
    }

    public String getId() {
        return id;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public String getFollowedId() {
        return followed_id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "UserToFollowedUser{" +
                "id='" + id + '\'' +
                ", user=" + (userDTO != null ? userDTO.getId() : null) +
                ", followed_id='" + followed_id + '\'' +
                ", time=" + time +
                '}';
    }
}
