package org.gamed.timelineservice.services;

import org.gamed.timelineservice.domain.GameDTO;
import org.gamed.timelineservice.domain.GameListDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListRetrievalServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ListRetrievalService listRetrievalService;

    private LinkedHashMap<String, Object> mockListData;
    private List<LinkedHashMap<String, Object>> mockListDataCollection;
    private GameDTO mockGame;
    private LinkedHashMap<String, String> mockGameMapping;

    @BeforeEach
    void setUp() {
        mockListData = new LinkedHashMap<>();
        mockListData.put("id", "list1");
        mockListData.put("name", "Favorite Games");
        mockListData.put("description", "A list of favorite games");
        mockListData.put("timeCreated", LocalDateTime.now().toString());
        mockListData.put("timeModified", LocalDateTime.now().toString());

        mockListDataCollection = Collections.singletonList(mockListData);

        mockGame = new GameDTO();
        mockGame.setId("game1");
        mockGame.setName("Test Game");
        mockGameMapping = new LinkedHashMap<>();
        mockGameMapping.put("game", "game1");
    }

    @Test
    void testRequestUserCreatedLists_success() {
        String userId = "user1";

        when(restTemplate.exchange(
                "http://localhost:8092/lists/user/" + userId,
                HttpMethod.GET,
                null,
                List.class))
                .thenReturn(ResponseEntity.ok(mockListDataCollection));

        when(restTemplate.exchange(
                "http://localhost:8092/listToGames/list/list1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LinkedHashMap<String, String>>>() {}))
                .thenReturn(ResponseEntity.ok(Collections.singletonList(mockGameMapping)));

        when(restTemplate.exchange(
                "http://localhost:8092/games/game1",
                HttpMethod.GET,
                null,
                GameDTO.class))
                .thenReturn(ResponseEntity.ok(mockGame));

        List<GameListDTO> result = listRetrievalService.requestUserCreatedLists(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        GameListDTO gameList = result.get(0);
        assertEquals("list1", gameList.getId());
        assertEquals("Favorite Games", gameList.getName());
        assertEquals("A list of favorite games", gameList.getDescription());
        assertEquals(1, gameList.getGames().size());
        assertEquals("Test Game", gameList.getGames().get(0).getName());
    }

    @Test
    void testRequestUserCreatedLists_emptyResponse() {
        String userId = "user_no_lists";

        when(restTemplate.exchange(
                "http://localhost:8092/lists/user/" + userId,
                HttpMethod.GET,
                null,
                List.class))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        List<GameListDTO> result = listRetrievalService.requestUserCreatedLists(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testRetrieveList_success() {
        String listId = "list1";

        when(restTemplate.exchange(
                "http://localhost:8092/lists/" + listId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {}))
                .thenReturn(ResponseEntity.ok(mockListData));

        when(restTemplate.exchange(
                "http://localhost:8092/listToGames/list/list1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LinkedHashMap<String, String>>>() {}))
                .thenReturn(ResponseEntity.ok(Collections.singletonList(mockGameMapping)));

        when(restTemplate.exchange(
                "http://localhost:8092/games/game1",
                HttpMethod.GET,
                null,
                GameDTO.class))
                .thenReturn(ResponseEntity.ok(mockGame));

        GameListDTO result = listRetrievalService.retrieveList(listId);

        assertNotNull(result);
        assertEquals("list1", result.getId());
        assertEquals("Favorite Games", result.getName());
        assertEquals("A list of favorite games", result.getDescription());
        assertEquals(1, result.getGames().size());
        assertEquals("Test Game", result.getGames().get(0).getName());
    }
}
