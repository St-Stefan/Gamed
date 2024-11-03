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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FollowService {
    private final RestTemplate restTemplate;
    private final String followedUserDatabaseUrl = "http://localhost:8090/user/followed-users";
    private final String followedListDatabaseUrl = "http://localhost:8090/user/followed-lists";
    private final String gameListDatabaseUrl = "http://localhost:8092/lists";
    private final UserService userService;

    public FollowService(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    public List<GameListDTO> requestFollowedLists (String userId) {
        ResponseEntity<List> response = null;

        try {
            response = restTemplate.exchange(
                    followedListDatabaseUrl + "/" + userId + "/following",
                    HttpMethod.GET,
                    null,
                    List.class
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }

        List<String> followedListsIds = response.getBody();
        List<GameListDTO> followedLists = new ArrayList<>();

        followedListsIds.forEach(followedListId -> {
            ResponseEntity<GameListDTO> gameListResponse = null;
            gameListResponse = restTemplate.exchange(
                    gameListDatabaseUrl + "/" + followedListId,
                    HttpMethod.GET,
                    null,
                    GameListDTO.class
            );
            ResponseEntity<List<LinkedHashMap<String, String>>> gamesInListResponse = null;
            gamesInListResponse = restTemplate.exchange(
                    "http://localhost:8092/listToGames/list/" + followedListId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            List<GameDTO> games = new ArrayList<>();

            gamesInListResponse.getBody().forEach( resp -> {
                ResponseEntity<GameDTO> gameInfo = null;
                System.out.println(resp);
                gameInfo = restTemplate.exchange(
                        "http://localhost:8092/games/" + resp.get("game"),
                        HttpMethod.GET,
                        null,
                        GameDTO.class
                );
                games.add(gameInfo.getBody());
            });

            GameListDTO list = gameListResponse.getBody();
            list.setGames(games);

            followedLists.add(gameListResponse.getBody());
        });

        return followedLists;
    }

    public List<UserDTO> requestFollowedUsers (String userId) {
        ResponseEntity<List> response = null;

        try {
            response = restTemplate.exchange(
                    followedUserDatabaseUrl + "/" + userId + "/following",
                    HttpMethod.GET,
                    null,
                    List.class
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }

        List<String> followedUsersIds = response.getBody();
        List<UserDTO> followedUsers = new ArrayList<>();

        followedUsersIds.forEach(followedId -> {
            UserDTO user = userService.requestUserInfo(followedId);
            followedUsers.add(user);
        });

        return followedUsers;
    }

    public List<UserDTO> requestFollowerUsers (String userId) {
        ResponseEntity<List> response = null;

        try {
            response = restTemplate.exchange(
                    followedUserDatabaseUrl + "/" + userId + "/followers",
                    HttpMethod.GET,
                    null,
                    List.class
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }

        List<String> followerUsersIds = response.getBody();
        List<UserDTO> followerUsers = new ArrayList<>();

        followerUsersIds.forEach(followedId -> {
            UserDTO user = userService.requestUserInfo(followedId);
            followerUsers.add(user);
        });

        return followerUsers;
    }
}
