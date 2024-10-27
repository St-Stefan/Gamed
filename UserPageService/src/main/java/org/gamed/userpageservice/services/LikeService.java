package org.gamed.userpageservice.services;

import org.gamed.userpageservice.domain.DTOs.GameDTO;
import org.gamed.userpageservice.domain.DTOs.GameListDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LikeService {
    private static RestTemplate restTemplate = new RestTemplate();
    private static final String userLikeDatabaseUrl = "http://localhost:8090/user/likes";
    private static final String gameDatabaseUrl = "http://localhost:8092/games";
    private static final String gameListDatabaseUrl = "http://localhost:8092/lists";

    public LikeService(RestTemplate rest) {
        restTemplate = rest;
    }

    public static List<LinkedHashMap<String, String>> requestUserLikeInfo (String userId) {
        ResponseEntity<List> response = null;

        try {
            response = restTemplate.exchange(
                    userLikeDatabaseUrl + "/" + userId + "/liked-items",
                    HttpMethod.GET,
                    null,
                    List.class
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }

        return response.getBody();
    }

    public static List<GameDTO> requestLikedGames (List<String> likedGamesIds) {
        List<GameDTO> likedGames = new ArrayList<>();
        try {
            likedGamesIds.forEach(likedGameId -> {
                ResponseEntity<GameDTO> gameResponse = null;
                gameResponse = restTemplate.exchange(
                        gameDatabaseUrl + "/" + likedGameId,
                        HttpMethod.GET,
                        null,
                        GameDTO.class
                );
                likedGames.add(gameResponse.getBody());
            });
        } catch (HttpClientErrorException e) {
            return null;
        }

        return likedGames;
    }

    public static List<GameListDTO> requestLikedLists (List<String> likedListsIds) {
        List<GameListDTO> likedLists = new ArrayList<>();

        try {
            likedListsIds.forEach(likedListId -> {
                ResponseEntity<GameListDTO> gameListResponse = null;
                gameListResponse = restTemplate.exchange(
                        gameListDatabaseUrl + "/" + likedListId,
                        HttpMethod.GET,
                        null,
                        GameListDTO.class
                );
                ResponseEntity<List<LinkedHashMap<String, String>>> gamesInListResponse = null;
                gamesInListResponse = restTemplate.exchange(
                        "http://localhost:8092/listToGames/list/" + likedListId,
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

                GameListDTO list = gameListResponse.getBody();
                list.setGames(games);
                likedLists.add(list);
            });
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }

        return likedLists;
    }
}
