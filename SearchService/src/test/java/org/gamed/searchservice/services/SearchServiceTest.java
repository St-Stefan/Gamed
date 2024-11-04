package org.gamed.searchservice.services;

import org.gamed.searchservice.domain.GameDTO;
import org.gamed.searchservice.models.SearchResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private SearchService searchService;

    private GameDTO game1;
    private GameDTO game2;
    private List<GameDTO> game = new ArrayList<>();
    private List<GameDTO> second = new ArrayList<>();

    @BeforeEach
    void setUp() {
        game1 = new GameDTO();
        game1.setId("game1");
        game1.setName("Game 1");

        game2 = new GameDTO();
        game2.setId("game2");
        game2.setName("Game the Second");

        game.add(game1);
        game.add(game2);

        second.add(game2);
    }

    @Test
    void testSearch_success() {
        String query = "Game";

        when(restTemplate.exchange(
                "http://localhost:8092/games/search/" + query,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GameDTO>>() {}))
                .thenReturn(ResponseEntity.ok(game));

        List<SearchResponseModel> searchResult = searchService.searchGames(query);
        assertNotNull(searchResult);
        assertEquals(2, searchResult.size());
        assertEquals(game1.getName(), searchResult.get(0).getGame().getName());
        assertEquals(game2.getName(), searchResult.get(1).getGame().getName());
    }

    @Test
    void testSearch_error() {
        String query = "error";

        when(restTemplate.exchange(
                "http://localhost:8092/games/search/" + query,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GameDTO>>() {}))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertNull(searchService.searchGames(query));
    }
}
