package org.gamed.userpageservice.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserPage {
    private User user;

    public UserPage(User user) {
        this.user = user;
    }


}
