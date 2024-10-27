package org.gamed.userpageservice.services;

import org.gamed.userpageservice.domain.DTOs.PlaytimeDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PlaytimeService {
    private static final String userPlaytimeDatabaseUrl = "http://localhost:8090/user/playtime";
    private static RestTemplate restTemplate = new RestTemplate();

    public PlaytimeService(RestTemplate rest) {
        restTemplate = rest;
    }

    public static List<PlaytimeDTO> requestPlaytime (String userId) {
        ResponseEntity<List> response = null;

        try {
            response = restTemplate.exchange(
                    userPlaytimeDatabaseUrl + "/" + userId + "/records",
                    HttpMethod.GET,
                    null,
                    List.class
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }

        List<LinkedHashMap<String, Object>> playtimesList = response.getBody();
        List<PlaytimeDTO> playtimes = new ArrayList<>();

        playtimesList.forEach(playtime -> {
            String playtimeId = (String) playtime.get("id");
            int time = (int) playtime.get("playtime");
            String gameId = (String) playtime.get("gameId");
            LocalDateTime timeCreated = LocalDateTime.parse((String) playtime.get("timeCreated"));
            LocalDateTime timeModified = LocalDateTime.parse((String) playtime.get("timeModified"));
            PlaytimeDTO playtimeDTO = new PlaytimeDTO(userId, playtimeId, gameId, time, timeCreated, timeModified);
            playtimes.add(playtimeDTO);
        });

        return playtimes;
    }
}
