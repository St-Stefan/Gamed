package org.gamed.timelineservice.services;

import org.gamed.timelineservice.domain.*;
import org.gamed.timelineservice.models.PostRequestResponseModel;
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
public class UserRetrievalServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ListRetrievalService listRetrievalService;

    @InjectMocks
    private UserRetrievalService userRetrievalService;

    private UserDTO userDTO;
    private GameDTO gameDTO;
    private ReviewDTO reviewDTO;
    private GameListDTO gameListDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId("user1");
        userDTO.setName("John Doe");

        gameDTO = new GameDTO();
        gameDTO.setId("game1");
        gameDTO.setName("Test Game");

        reviewDTO = new ReviewDTO();
        reviewDTO.setGameId("game1");
        reviewDTO.setUserId("user1");
        reviewDTO.setDescription("A great game!");
        reviewDTO.setTimeCreated(LocalDateTime.now());

        gameListDTO = new GameListDTO("list1", "user1", "My List", "List of games", LocalDateTime.now(), LocalDateTime.now(), List.of(gameDTO));
    }

    @Test
    void testRetrieveUser_success() {
        String userId = "user1";
        when(restTemplate.getForEntity("http://localhost:8090/users/" + userId, UserDTO.class))
                .thenReturn(ResponseEntity.ok(userDTO));

        UserDTO result = userRetrievalService.retrieveUser(userId);

        assertNotNull(result);
        assertEquals("user1", result.getId());
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testRetrieveFollowList_success() {
        String userId = "user1";
        List<String> mockFollowList = Arrays.asList("user2", "user3");

        when(restTemplate.exchange(
                "http://localhost:8090/user/followed-users/" + userId + "/following",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}))
                .thenReturn(ResponseEntity.ok(mockFollowList));

        List<String> followList = userRetrievalService.retrieveFollowList(userId);

        assertNotNull(followList);
        assertEquals(2, followList.size());
        assertTrue(followList.contains("user2"));
        assertTrue(followList.contains("user3"));
    }

    @Test
    void testRetrieveFollowList_empty() {
        String userId = "user_no_follows";
        when(restTemplate.exchange(
                "http://localhost:8090/user/followed-users/" + userId + "/following",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        List<String> followList = userRetrievalService.retrieveFollowList(userId);

        assertNotNull(followList);
        assertTrue(followList.isEmpty());
    }

    @Test
    void testRetrieveAllPosts_success() {
        List<String> followList = Arrays.asList("user1");
        List<ReviewDTO> mockReviews = Collections.singletonList(reviewDTO);

        when(restTemplate.exchange(
                "http://localhost:8091/reviews/user/user1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReviewDTO>>() {}))
                .thenReturn(ResponseEntity.ok(mockReviews));

        when(restTemplate.getForEntity("http://localhost:8090/users/user1", UserDTO.class))
                .thenReturn(ResponseEntity.ok(userDTO));

        gameDTO.setName("Test Game");
        when(restTemplate.getForEntity("http://localhost:8092/games/game1", GameDTO.class))
                .thenReturn(ResponseEntity.ok(gameDTO));

        List<PostRequestResponseModel> posts = userRetrievalService.retrieveAllPosts(followList);

        assertNotNull(posts);
        assertEquals(1, posts.size());
        PostRequestResponseModel post = posts.get(0);

        assertEquals("A great game!", post.getContent());
        assertEquals("John Doe", post.getAuthor());
    }

    @Test
    void testRetrieveAllLists_success() {
        List<String> followList = Arrays.asList("user1");
        List<GameListDTO> mockGameLists = Collections.singletonList(gameListDTO);

        when(listRetrievalService.requestUserCreatedLists("user1"))
                .thenReturn(mockGameLists);

        when(restTemplate.getForEntity("http://localhost:8090/users/user1", UserDTO.class))
                .thenReturn(ResponseEntity.ok(userDTO));

        List<PostRequestResponseModel> posts = userRetrievalService.retrieveAllLists(followList);

        assertNotNull(posts);
        assertEquals(1, posts.size());
        PostRequestResponseModel post = posts.get(0);
        assertEquals("My List", post.getTitle());
        assertEquals("List of games", post.getContent());
        assertEquals("John Doe", post.getAuthor());
    }

    @Test
    void testRetrieveGame_success() {
        String gameId = "game1";
        when(restTemplate.getForEntity("http://localhost:8092/games/" + gameId, GameDTO.class))
                .thenReturn(ResponseEntity.ok(gameDTO));

        GameDTO result = userRetrievalService.retrieveGame(gameId);

        assertNotNull(result);
        assertEquals("game1", result.getId());
        assertEquals("Test Game", result.getName());
    }

    @Test
    void testRetrieveGame_notFound() {
        String gameId = "nonexistent_game";
        when(restTemplate.getForEntity("http://localhost:8092/games/" + gameId, GameDTO.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        GameDTO result = userRetrievalService.retrieveGame(gameId);

        assertNull(result);
    }

    @Test
    void testRetrieveAllPosts_noReviews() {
        List<String> followList = Arrays.asList("user1");

        when(restTemplate.exchange(
                "http://localhost:8091/reviews/user/user1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReviewDTO>>() {}))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        List<PostRequestResponseModel> posts = userRetrievalService.retrieveAllPosts(followList);

        assertNotNull(posts);
        assertTrue(posts.isEmpty());
    }

    @Test
    void testRetrieveAllLists_noLists() {
        List<String> followList = Arrays.asList("user1");

        when(listRetrievalService.requestUserCreatedLists("user1"))
                .thenReturn(Collections.emptyList());

        List<PostRequestResponseModel> posts = userRetrievalService.retrieveAllLists(followList);

        assertNotNull(posts);
        assertTrue(posts.isEmpty());
    }
}
