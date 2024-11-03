package org.gamed.userpageservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.gamed.userpageservice.DTOs.GameDTO;
import org.gamed.userpageservice.DTOs.GameListDTO;
import org.gamed.userpageservice.DTOs.LikeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LikeService likeService;

    private final String userLikeDatabaseUrl = "http://localhost:8090/user/likes";
    private final String gameDatabaseUrl = "http://localhost:8092/games";
    private final String gameListDatabaseUrl = "http://localhost:8092/lists";

    private LikeDTO mockLikeDTO1;
    private LikeDTO mockLikeDTO2;
    private GameDTO mockGameDTO1;
    private GameDTO mockGameDTO2;
    private GameListDTO mockGameListDTO1;
    private GameListDTO mockGameListDTO2;

    @BeforeEach
    public void setUp() {
        mockLikeDTO1 = new LikeDTO();
        mockLikeDTO2 = new LikeDTO();
        mockGameDTO1 = new GameDTO();
        mockGameDTO2 = new GameDTO();
        mockGameListDTO1 = new GameListDTO();
        mockGameListDTO2 = new GameListDTO();
    }

    @Test
    public void testRequestUserLikeInfo_Success() {
        String userId = "user123";
        List<LikeDTO> mockLikes = Arrays.asList(mockLikeDTO1, mockLikeDTO2);
        ResponseEntity<List<LikeDTO>> responseEntity = new ResponseEntity<>(mockLikes, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(userLikeDatabaseUrl + "/" + userId + "/liked-items"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseEntity);

        List<LikeDTO> result = likeService.requestUserLikeInfo(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testRequestUserLikeInfo_HttpClientErrorException() {
        String userId = "user123";

        when(restTemplate.exchange(
                eq(userLikeDatabaseUrl + "/" + userId + "/liked-items"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<LikeDTO> result = likeService.requestUserLikeInfo(userId);

        assertNull(result);
    }

    @Test
    public void testRequestUserLikeInfo_EmptyList() {
        String userId = "user123";
        List<LikeDTO> mockLikes = new ArrayList<>();
        ResponseEntity<List<LikeDTO>> responseEntity = new ResponseEntity<>(mockLikes, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(userLikeDatabaseUrl + "/" + userId + "/liked-items"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseEntity);

        List<LikeDTO> result = likeService.requestUserLikeInfo(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRequestLikedGames_Success() {
        List<String> likedGamesIds = Arrays.asList("game1", "game2");

        ResponseEntity<GameDTO> game1Response = new ResponseEntity<>(mockGameDTO1, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game1Response);

        ResponseEntity<GameDTO> game2Response = new ResponseEntity<>(mockGameDTO2, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game2Response);

        List<GameDTO> result = likeService.requestLikedGames(likedGamesIds);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testRequestLikedGames_HttpClientErrorException() {
        List<String> likedGamesIds = Arrays.asList("game1", "game2");

        ResponseEntity<GameDTO> game1Response = new ResponseEntity<>(mockGameDTO1, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game1Response);

        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<GameDTO> result = likeService.requestLikedGames(likedGamesIds);

        assertNull(result);
    }

    @Test
    public void testRequestLikedGames_EmptyList() {
        List<String> likedGamesIds = new ArrayList<>();

        List<GameDTO> result = likeService.requestLikedGames(likedGamesIds);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRequestLikedLists_Success() {
        List<String> likedListsIds = Arrays.asList("list1", "list2");

        ResponseEntity<GameListDTO> list1Response = new ResponseEntity<>(mockGameListDTO1, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/list1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class))
        ).thenReturn(list1Response);

        List<LinkedHashMap<String, String>> gamesInList1 = new ArrayList<>();
        LinkedHashMap<String, String> gameRef1 = new LinkedHashMap<>();
        gameRef1.put("game", "game1");
        gamesInList1.add(gameRef1);

        ResponseEntity<List<LinkedHashMap<String, String>>> gamesInList1Response =
                new ResponseEntity<>(gamesInList1, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8092/listToGames/list/list1"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(gamesInList1Response);

        ResponseEntity<GameDTO> game1Response = new ResponseEntity<>(mockGameDTO1, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game1Response);

        ResponseEntity<GameListDTO> list2Response = new ResponseEntity<>(mockGameListDTO2, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/list2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class))
        ).thenReturn(list2Response);

        List<LinkedHashMap<String, String>> gamesInList2 = new ArrayList<>();
        LinkedHashMap<String, String> gameRef2 = new LinkedHashMap<>();
        gameRef2.put("game", "game2");
        gamesInList2.add(gameRef2);

        ResponseEntity<List<LinkedHashMap<String, String>>> gamesInList2Response =
                new ResponseEntity<>(gamesInList2, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8092/listToGames/list/list2"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(gamesInList2Response);

        ResponseEntity<GameDTO> game2Response = new ResponseEntity<>(mockGameDTO2, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game2Response);

        List<GameListDTO> result = likeService.requestLikedLists(likedListsIds);

        assertNotNull(result);
        assertEquals(2, result.size());

        GameListDTO listDTO1 = result.get(0);
        assertEquals(mockGameListDTO1, listDTO1);
        assertNotNull(listDTO1.getGames());
        assertEquals(1, listDTO1.getGames().size());

        GameListDTO listDTO2 = result.get(1);
        assertEquals(mockGameListDTO2, listDTO2);
        assertNotNull(listDTO2.getGames());
        assertEquals(1, listDTO2.getGames().size());
    }

    @Test
    public void testRequestLikedLists_HttpClientErrorExceptionOnList() {
        List<String> likedListsIds = Arrays.asList("list1", "list2");

        ResponseEntity<GameListDTO> list1Response = new ResponseEntity<>(mockGameListDTO1, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/list1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class))
        ).thenReturn(list1Response);

        List<LinkedHashMap<String, String>> gamesInList1 = new ArrayList<>();
        LinkedHashMap<String, String> gameRef1 = new LinkedHashMap<>();
        gameRef1.put("game", "game1");
        gamesInList1.add(gameRef1);

        ResponseEntity<List<LinkedHashMap<String, String>>> gamesInList1Response =
                new ResponseEntity<>(gamesInList1, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8092/listToGames/list/list1"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(gamesInList1Response);

        ResponseEntity<GameDTO> game1Response = new ResponseEntity<>(mockGameDTO1, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game1Response);

        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/list2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<GameListDTO> result = likeService.requestLikedLists(likedListsIds);
        assertNull(result);
    }

    @Test
    public void testRequestLikedLists_PartialFailureInGames() {
        List<String> likedListsIds = Arrays.asList("list1");

        ResponseEntity<GameListDTO> list1Response = new ResponseEntity<>(mockGameListDTO1, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/list1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class))
        ).thenReturn(list1Response);

        List<LinkedHashMap<String, String>> gamesInList1 = new ArrayList<>();
        LinkedHashMap<String, String> gameRef1 = new LinkedHashMap<>();
        gameRef1.put("game", "game1");
        LinkedHashMap<String, String> gameRef2 = new LinkedHashMap<>();
        gameRef2.put("game", "game2");
        gamesInList1.add(gameRef1);
        gamesInList1.add(gameRef2);

        ResponseEntity<List<LinkedHashMap<String, String>>> gamesInList1Response =
                new ResponseEntity<>(gamesInList1, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8092/listToGames/list/list1"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(gamesInList1Response);

        ResponseEntity<GameDTO> game1Response = new ResponseEntity<>(mockGameDTO1, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game1Response);

        when(restTemplate.exchange(
                eq(gameDatabaseUrl + "/game2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<GameListDTO> result = likeService.requestLikedLists(likedListsIds);
        assertNull(result);
    }

    @Test
    public void testRequestLikedLists_EmptyList() {
        List<String> likedListsIds = new ArrayList<>();

        List<GameListDTO> result = likeService.requestLikedLists(likedListsIds);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
