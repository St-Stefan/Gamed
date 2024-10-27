package org.gamed.searchservice.service;

import org.gamed.searchservice.domain.GameDTO;
import org.gamed.searchservice.models.SearchResponseModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String gameDatabase = "http://localhost:8092/games/search/";

    public List<SearchResponseModel> searchGames(String query) {
        ResponseEntity<List> response;

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

        List<GameDTO> searchResults = response.getBody();

        List<SearchResponseModel> results = new ArrayList<>();
        for (GameDTO game : searchResults) {
            results.add(new SearchResponseModel(game));
        }

        return results;
    }
}
