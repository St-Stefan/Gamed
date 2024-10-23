package org.gamed.userpageservice.services;

import org.gamed.userpageservice.domain.DTOs.GameListDTO;
import org.gamed.userpageservice.domain.DTOs.UserDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UserService {
    private static RestTemplate restTemplate = new RestTemplate();
    private static final String userDatabaseUrl = "http://localhost:8090/users";
    private static final String gameListDatabaseUrl = "http://localhost:8092/lists";

    public static UserDTO requestUserInfo (String userId) {
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

    public static List<GameListDTO> requestUserCreatedLists (String userId) {
        ResponseEntity<List> response = null;

        try {
            response = restTemplate.exchange(
                    gameListDatabaseUrl + "/user/" + userId,
                    HttpMethod.GET,
                    null,
                    List.class
            );
        } catch (HttpClientErrorException e) {
            return null;
        }

        List<LinkedHashMap<String, Object>> createdLists = response.getBody();
        List<GameListDTO> createdGameLists = new ArrayList<>();

        createdLists.forEach(list -> {
            String id = (String) list.get("id");
            String name = (String) list.get("name");
            String description = (String) list.get("description");
            LocalDateTime created = LocalDateTime.parse((String) list.get("timeCreated"));
            LocalDateTime updated = LocalDateTime.parse((String) list.get("timeModified"));
            GameListDTO createdList = new GameListDTO(id, userId, name, description, created, updated);
            createdGameLists.add(createdList);
        });

        return createdGameLists;
    }
}
