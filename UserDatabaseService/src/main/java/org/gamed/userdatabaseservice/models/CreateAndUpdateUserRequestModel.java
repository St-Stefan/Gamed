package org.gamed.userdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAndUpdateUserRequestModel {
    private String username;
    private String email;
    private String pwdHash;
    private boolean developer;
    private boolean premium;
}
