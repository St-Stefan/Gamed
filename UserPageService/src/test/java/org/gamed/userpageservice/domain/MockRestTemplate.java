package org.gamed.userpageservice.domain;

import org.gamed.userpageservice.domain.DTOs.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.Mockito.when;

public class MockRestTemplate {
    private RestTemplate mockRestTemplate;

    private static final String userDatabaseUrl = "http://localhost:8090/users";
    private static final String userLikeDatabaseUrl = "http://localhost:8090/user/likes";
    private static final String gameDatabaseUrl = "http://localhost:8092/games";
    private static final String gameListDatabaseUrl = "http://localhost:8092/lists";
    private static final String followedUserDatabaseUrl = "http://localhost:8090/user/followed-users";
    private static final String followedListDatabaseUrl = "http://localhost:8090/user/followed-lists";
    private static final String userPlaytimeDatabaseUrl = "http://localhost:8090/user/playtime";

    public MockRestTemplate(RestTemplate mockRestTemplate) {
        this.mockRestTemplate = mockRestTemplate;
    }

    public void setUpMocks(UserPage userPage) {
        setUpUserInfo(userPage);
        setUpLikedItems(userPage);
        setUpLists(userPage);
        setUpGames(userPage);
        setUpFollowData(userPage);
        setUpPlaytimes(userPage);
    }

    private void setUpUserInfo(UserPage userPage) {
        UserDTO user = userPage.getUserDTO();

        when(mockRestTemplate.exchange(
                userDatabaseUrl + "/" + user.getId(),
                HttpMethod.GET,
                null,
                UserDTO.class
        )).thenReturn(new ResponseEntity<>(user, HttpStatus.OK));

        List<UserDTO> allUsers = new ArrayList<>();
        allUsers.addAll(userPage.getFollowedUsers());
        allUsers.addAll(userPage.getFollowerUsers());

        for (UserDTO otherUser : allUsers) {
            when(mockRestTemplate.exchange(
                    userDatabaseUrl + "/" + otherUser.getId(),
                    HttpMethod.GET,
                    null,
                    UserDTO.class
            )).thenReturn(new ResponseEntity<>(otherUser, HttpStatus.OK));
        }

        List<LinkedHashMap<String, Object>> createdLists = new ArrayList<>();
        for (GameListDTO listDTO : userPage.getCreatedGameLists()) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("id", listDTO.getId());
            map.put("name", listDTO.getName());
            map.put("description", listDTO.getDescription());
            map.put("timeCreated", listDTO.getTime_created().toString());
            map.put("timeModified", listDTO.getTime_modified().toString());
            createdLists.add(map);
        }

        when(mockRestTemplate.exchange(
                gameListDatabaseUrl + "/user/" + user.getId(),
                HttpMethod.GET,
                null,
                List.class
        )).thenReturn(new ResponseEntity<>(createdLists, HttpStatus.OK));
    }

    private void setUpLikedItems(UserPage userPage) {
        List<LinkedHashMap<String, String>> likedMap = new ArrayList<>();

        for (GameListDTO listItem : userPage.getLikedLists()) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put(listItem.getId(), "List");
            likedMap.add(map);
        }

        for (GameDTO gameItem : userPage.getLikedGames()) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put(gameItem.getId(), "Game");
            likedMap.add(map);
        }

        when(mockRestTemplate.exchange(
                userLikeDatabaseUrl + "/" + userPage.getUserDTO().getId() + "/liked-items",
                HttpMethod.GET,
                null,
                List.class
        )).thenReturn(new ResponseEntity<>(likedMap, HttpStatus.OK));
    }

    private void setUpLists(UserPage userPage) {
        List<GameListDTO> allLists = new ArrayList<>();
        allLists.addAll(userPage.getLikedLists());
        allLists.addAll(userPage.getFollowedLists());
        allLists.addAll(userPage.getCreatedGameLists());

        for (GameListDTO listDTO : allLists) {
            when(mockRestTemplate.exchange(
                    gameListDatabaseUrl + "/" + listDTO.getId(),
                    HttpMethod.GET,
                    null,
                    GameListDTO.class
            )).thenReturn(new ResponseEntity<>(listDTO, HttpStatus.OK));
        }
    }

    private void setUpGames(UserPage userPage) {
        for (GameDTO gameDTO : userPage.getLikedGames()) {
            when(mockRestTemplate.exchange(
                    gameDatabaseUrl + "/" + gameDTO.getId(),
                    HttpMethod.GET,
                    null,
                    GameDTO.class
            )).thenReturn(new ResponseEntity<>(gameDTO, HttpStatus.OK));
        }
    }

    private void setUpFollowData(UserPage userPage) {
        when(mockRestTemplate.exchange(
                followedUserDatabaseUrl + "/" + userPage.getUserDTO().getId() + "/following",
                HttpMethod.GET,
                null,
                List.class
        )).thenReturn(new ResponseEntity<>(
                userPage.getFollowedUsers().stream().map(UserDTO::getId).toList(),
                HttpStatus.OK));

        when(mockRestTemplate.exchange(
                followedUserDatabaseUrl + "/" + userPage.getUserDTO().getId() + "/followers",
                HttpMethod.GET,
                null,
                List.class
        )).thenReturn(new ResponseEntity<>(
                userPage.getFollowerUsers().stream().map(UserDTO::getId).toList(),
                HttpStatus.OK));

        when(mockRestTemplate.exchange(
                followedListDatabaseUrl + "/" + userPage.getUserDTO().getId() + "/following",
                HttpMethod.GET,
                null,
                List.class
        )).thenReturn(new ResponseEntity<>(
                userPage.getFollowedLists().stream().map(GameListDTO::getId).toList(),
                HttpStatus.OK));
    }

    private void setUpPlaytimes(UserPage userPage) {
        List<LinkedHashMap<String, Object>> playtimeList = new ArrayList<>();
        for (PlaytimeDTO playtime : userPage.getPlaytimes()) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("id", playtime.getId());
            map.put("playtime", playtime.getPlaytime());
            map.put("gameId", playtime.getGameId());
            playtimeList.add(map);
        }

        when(mockRestTemplate.exchange(
                userPlaytimeDatabaseUrl + "/" + userPage.getUserDTO().getId() + "/records",
                HttpMethod.GET,
                null,
                List.class
        )).thenReturn(new ResponseEntity<>(playtimeList, HttpStatus.OK));
    }

    public RestTemplate getMockRestTemplate() {
        return mockRestTemplate;
    }
}
