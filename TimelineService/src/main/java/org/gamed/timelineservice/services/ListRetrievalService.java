package org.gamed.timelineservice.services;

import org.gamed.timelineservice.domain.GameDTO;
import org.gamed.timelineservice.domain.GameListDTO;
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
public class ListRetrievalService {
    private static final RestTemplate restTemplate = new RestTemplate();
    private final String userDatabaseURL = "http://localhost:8090/users";
    private final String followServiceURL = "http://localhost:8090/user/followed-users";
    private final String reviewServiceURL = "http://localhost:8091/reviews";

    public static List<GameListDTO> requestUserCreatedLists (String userId) {
        ResponseEntity<List> response = null;

        try {
            String gameListDatabaseURL = "http://localhost:8092/lists";
            response = restTemplate.exchange(
                    gameListDatabaseURL + "/user/" + userId,
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
