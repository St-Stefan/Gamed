package org.gamed.searchservice.services;

import org.gamed.searchservice.domain.GameDTO;
import org.gamed.searchservice.models.SearchResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SearchService searchService;

    private GameDTO game1;
    private GameDTO game2;
    private List<GameDTO> game = new ArrayList<>();
    private List<SearchResponseModel> srGame = new ArrayList<>();
    private List<GameDTO> second = new ArrayList<>();
    private List<GameDTO> noresults = new ArrayList<>();

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

        srGame.add(new SearchResponseModel(game1));
        srGame.add(new SearchResponseModel(game2));

        second.add(game2);
    }

    @Test
    void testSearch_success() {
        when(searchService.searchGames("game")).thenReturn(srGame);
        List<SearchResponseModel> searchResult = searchService.searchGames("game");

        assertEquals(2, searchResult.size());
        for (SearchResponseModel s : searchResult) {
            assertTrue(game.contains(s.getGame()));
        }
    }
}
