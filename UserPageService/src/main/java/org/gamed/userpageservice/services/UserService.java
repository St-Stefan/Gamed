package org.gamed.userpageservice.services;

import org.gamed.userpageservice.domain.DTOs.UserDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class UserService {
    private static RestTemplate restTemplate = new RestTemplate();
    private static final String userDatabaseUrl = "http://localhost:8090/users";

    public static UserDTO requestUserInfo(String userId) {
        ResponseEntity<UserDTO> response = null;

        try {
            response = restTemplate.exchange(
                    userDatabaseUrl + "/" + userId,
                    HttpMethod.GET,
                    null,
                    UserDTO.class
            );
        } catch (HttpClientErrorException e) {
            return null;
        }

        return response.getBody();
    }
}
