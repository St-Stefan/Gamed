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
public class LikeRetrievalServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserRetrievalService userRetrievalService;

    @Mock
    private ListRetrievalService listRetrievalService;

    @InjectMocks
    private LikeRetrievalService likeRetrievalService;

    private UserDTO userDTO;
    private GameDTO gameDTO;
    private GameListDTO gameListDTO;
    private LikeDTO likeDTOGame;
    private LikeDTO likeDTOList;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId("user1");
        userDTO.setName("John Doe");

        gameDTO = new GameDTO();
        gameDTO.setId("game1");
        gameDTO.setName("Test Game");

        gameListDTO = new GameListDTO("list1", "user1", "My List", "A list of games", LocalDateTime.now(), LocalDateTime.now(), List.of(gameDTO));

        likeDTOGame = new LikeDTO("user1", "game1", "game");
        likeDTOGame.setTime(LocalDateTime.now());

        likeDTOList = new LikeDTO("user1", "list1", "list");
        likeDTOList.setTime(LocalDateTime.now());
    }

    @Test
    void testRequestLikes_success() {
        String userId = "user1";
        List<LikeDTO> mockLikes = List.of(likeDTOGame, likeDTOList);

        when(restTemplate.exchange(
                "http://localhost:8090/user/likes/" + userId + "/liked-items",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LikeDTO>>() {}))
                .thenReturn(ResponseEntity.ok(mockLikes));

        List<LikeDTO> likes = likeRetrievalService.requestLikes(userId);

        assertNotNull(likes);
        assertEquals(2, likes.size());
        assertEquals("game", likes.get(0).getType());
        assertEquals("list", likes.get(1).getType());
    }

    @Test
    void testRequestLikes_error() {
        String userId = "user1";

        when(restTemplate.exchange(
                "http://localhost:8090/user/likes/" + userId + "/liked-items",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LikeDTO>>() {}))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class, () -> likeRetrievalService.requestLikes(userId));
    }

    @Test
    void testConvertLikeDtoToPostRequestResponseModel_gameLike() {
        when(userRetrievalService.retrieveUser("user1")).thenReturn(userDTO);
        when(userRetrievalService.retrieveGame("game1")).thenReturn(gameDTO);

        PostRequestResponseModel post = likeRetrievalService.convertLikeDtoToPostRequestResponseModel(likeDTOGame);

        assertNotNull(post);
        assertEquals("Test Game", post.getTitle());
        assertEquals("John Doe likes the game:", post.getContent());
        assertEquals("John Doe", post.getAuthor());
        assertTrue(post.isGameLike());
        assertFalse(post.isListLike());
        assertEquals(1, post.getGames().size());
        assertEquals("Test Game", post.getGames().get(0).getName());
    }

    @Test
    void testConvertLikeDtoToPostRequestResponseModel_listLike() {
        when(userRetrievalService.retrieveUser("user1")).thenReturn(userDTO);
        when(listRetrievalService.retrieveList("list1")).thenReturn(gameListDTO);

        PostRequestResponseModel post = likeRetrievalService.convertLikeDtoToPostRequestResponseModel(likeDTOList);

        assertNotNull(post);
        assertEquals("My List", post.getTitle());
        assertEquals("John Doe has liked the list A list of games", post.getContent());
        assertEquals("John Doe", post.getAuthor());
        assertTrue(post.isListLike());
        assertFalse(post.isGameLike());
        assertEquals(1, post.getGames().size());
        assertEquals("Test Game", post.getGames().get(0).getName());
    }

    @Test
    void testConvertLikeDtoToPostRequestResponseModel_unknownType() {
        LikeDTO likeDTOUnknown = new LikeDTO("user1", "unknown_item", "unknown");

        assertThrows(NullPointerException.class,
                () -> likeRetrievalService.convertLikeDtoToPostRequestResponseModel(likeDTOUnknown));
    }

    @Test
    void testConvertLikeDtoToPostRequestResponseModel_nullLike() {
        assertThrows(IllegalArgumentException.class,
                () -> likeRetrievalService.convertLikeDtoToPostRequestResponseModel(null));
    }

    @Test
    void testGetLikePosts_success() {
        String userId = "user1";
        List<LikeDTO> mockLikes = List.of(likeDTOGame, likeDTOList);

        when(restTemplate.exchange(
                "http://localhost:8090/user/likes/" + userId + "/liked-items",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LikeDTO>>() {}))
                .thenReturn(ResponseEntity.ok(mockLikes));

        when(userRetrievalService.retrieveUser("user1")).thenReturn(userDTO);
        when(userRetrievalService.retrieveGame("game1")).thenReturn(gameDTO);
        when(listRetrievalService.retrieveList("list1")).thenReturn(gameListDTO);

        List<PostRequestResponseModel> posts = likeRetrievalService.getLikePosts(userId);

        assertNotNull(posts);
        assertEquals(2, posts.size());
    }
}
