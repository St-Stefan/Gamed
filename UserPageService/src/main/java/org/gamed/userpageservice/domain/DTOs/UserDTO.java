package org.gamed.userpageservice.domain.DTOs;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private String id;

    private String name;

    private String email;

    private String pwdHash;

    private boolean developer;

    private boolean premium;

    private LocalDateTime timeCreated;

    private LocalDateTime timeModified;

    public UserDTO(String id, String name, String email, String pwdHash, boolean developer, boolean premium,
                   LocalDateTime timeCreated, LocalDateTime timeModified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pwdHash = pwdHash;
        this.developer = developer;
        this.premium = premium;
        this.timeCreated = timeCreated;
        this.timeModified = timeModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return this.id.equals(userDTO.id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", developer=" + developer +
                ", premium=" + premium +
                ", time_created=" + timeCreated +
                ", time_modified=" + timeModified +
                '}';
    }
}
