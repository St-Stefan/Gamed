package org.gamed.userpageservice.services;

import org.gamed.userpageservice.DTOs.GameDTO;
import org.gamed.userpageservice.DTOs.GameListDTO;
import org.gamed.userpageservice.DTOs.UserDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class UserService {
    private final RestTemplate restTemplate;
    private final String userDatabaseUrl = "http://localhost:8090/users";
    private final String gameListDatabaseUrl = "http://localhost:8092/lists";

    public UserService(RestTemplate rest) {
        restTemplate = rest;
    }

    public UserDTO requestUserInfo (String userId) {
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

    public List<GameListDTO> requestUserCreatedLists (String userId) {
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
            ResponseEntity<List<LinkedHashMap<String, String>>> gamesInListResponse = null;
            gamesInListResponse = restTemplate.exchange(
                    "http://localhost:8092/listToGames/list/" + list.get("id").toString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            List<GameDTO> games = new ArrayList<>();

            gamesInListResponse.getBody().forEach( resp -> {
                ResponseEntity<GameDTO> gameInfo = null;
                gameInfo = restTemplate.exchange(
                        "http://localhost:8092/games/" + resp.get("game"),
                        HttpMethod.GET,
                        null,
                        GameDTO.class
                );
                games.add(gameInfo.getBody());
            });

            String id = (String) list.get("id");
            String name = (String) list.get("name");
            String description = (String) list.get("description");
            LocalDateTime created = LocalDateTime.parse((String) list.get("timeCreated"));
            LocalDateTime updated = LocalDateTime.parse((String) list.get("timeModified"));
            GameListDTO createdList = new GameListDTO(id, userId, name, description, created, updated, games);
            createdGameLists.add(createdList);
        });

        return createdGameLists;
    }
}
