package org.gamed.searchservice.service;

import org.gamed.searchservice.domain.GameDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SearchService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String gameDatabase = "http://localhost:8092/games/search/";

    public List<GameDTO> searchGames(String query) {
        ResponseEntity<List> response = null;

        try {
            response = restTemplate.exchange(
                    gameDatabase + query,
                HttpMethod.GET,
                null,
                List.class
            );
        } catch (HttpClientErrorException e) {
            return null;
        }

        return response.getBody();

    }
}
