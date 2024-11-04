package org.gamed.searchservice.services;

import org.gamed.searchservice.domain.GameDTO;
import org.gamed.searchservice.models.SearchResponseModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private final RestTemplate restTemplate;
    private final String gameDatabase = "http://localhost:8092/games/search/";

    public SearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SearchResponseModel> searchGames(String query) {
        ResponseEntity<List<GameDTO>> response;

        try {
            response = restTemplate.exchange(
                    gameDatabase + query,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
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
