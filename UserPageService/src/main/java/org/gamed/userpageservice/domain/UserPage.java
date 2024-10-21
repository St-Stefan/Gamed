package org.gamed.userpageservice.domain;

import lombok.Getter;
import lombok.ToString;
import org.gamed.userpageservice.domain.DTOs.UserDTO;

@Getter
@ToString
public class UserPage {
    private UserDTO userDTO;

    public UserPage(UserDTO userDTO) {
        this.userDTO = userDTO;
    }


}
