package org.gamed.userpageservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.gamed.userpageservice.DTOs.GameDTO;
import org.gamed.userpageservice.DTOs.GameListDTO;
import org.gamed.userpageservice.DTOs.UserDTO;
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

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserService userService;

    @InjectMocks
    private FollowService followService;

    private final String followedUserDatabaseUrl = "http://localhost:8090/user/followed-users";
    private final String followedListDatabaseUrl = "http://localhost:8090/user/followed-lists";
    private final String gameListDatabaseUrl = "http://localhost:8092/lists";

    private GameDTO mockGameDTO1;
    private GameDTO mockGameDTO2;
    private GameListDTO mockGameListDTO1;
    private GameListDTO mockGameListDTO2;
    private UserDTO mockUserDTO1;
    private UserDTO mockUserDTO2;

    @BeforeEach
    public void setUp() {
        mockGameDTO1 = new GameDTO(
                "game1",
                "Game One",
                "Developer A",
                LocalDateTime.of(2020, 5, 20, 0, 0),
                "PC, PS4"
        );

        mockGameDTO2 = new GameDTO(
                "game2",
                "Game Two",
                "Developer B",
                LocalDateTime.of(2021, 8, 15, 0, 0),
                "Xbox, Switch"
        );

        mockGameListDTO1 = new GameListDTO();
        mockGameListDTO1.setId("list1");
        mockGameListDTO1.setUserId("user123");
        mockGameListDTO1.setName("Favorites");
        mockGameListDTO1.setDescription("My favorite games");
        mockGameListDTO1.setTimeCreated(LocalDateTime.of(2022, 1, 10, 10, 0));
        mockGameListDTO1.setTimeModified(LocalDateTime.of(2022, 1, 15, 12, 0));
        mockGameListDTO1.setGames(new ArrayList<>());

        mockGameListDTO2 = new GameListDTO();
        mockGameListDTO2.setId("list2");
        mockGameListDTO2.setUserId("user123");
        mockGameListDTO2.setName("To Play");
        mockGameListDTO2.setDescription("Games I want to play");
        mockGameListDTO2.setTimeCreated(LocalDateTime.of(2022, 2, 5, 9, 30));
        mockGameListDTO2.setTimeModified(LocalDateTime.of(2022, 2, 6, 11, 45));
        mockGameListDTO2.setGames(new ArrayList<>());

        mockUserDTO1 = new UserDTO();
        mockUserDTO1.setId("user456");
        mockUserDTO1.setName("Follower1");
        mockUserDTO1.setEmail("follower1@example.com");

        mockUserDTO2 = new UserDTO();
        mockUserDTO2.setId("user789");
        mockUserDTO2.setName("Follower2");
        mockUserDTO2.setEmail("follower2@example.com");
    }

    @Test
    public void testRequestFollowedLists_Success() {
        String userId = "user123";
        List<String> followedListsIds = Arrays.asList("list1", "list2");

        ResponseEntity<List> followedListsResponse = new ResponseEntity<>(followedListsIds, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(followedListDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(followedListsResponse);

        ResponseEntity<GameListDTO> list1Response = new ResponseEntity<>(mockGameListDTO1, HttpStatus.OK);
        ResponseEntity<GameListDTO> list2Response = new ResponseEntity<>(mockGameListDTO2, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/list1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class))
        ).thenReturn(list1Response);

        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/list2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class))
        ).thenReturn(list2Response);

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
                eq("http://localhost:8092/games/game1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game1Response);

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
                eq("http://localhost:8092/games/game2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(game2Response);

        List<GameListDTO> result = followService.requestFollowedLists(userId);

        assertNotNull(result);
        assertEquals(2, result.size());

        GameListDTO list1 = result.get(0);
        assertEquals("list1", list1.getId());
        assertEquals("Favorites", list1.getName());
        assertEquals("My favorite games", list1.getDescription());
        assertEquals(LocalDateTime.of(2022, 1, 10, 10, 0), list1.getTimeCreated());
        assertEquals(LocalDateTime.of(2022, 1, 15, 12, 0), list1.getTimeModified());
        assertNotNull(list1.getGames());
        assertEquals(1, list1.getGames().size());
        assertEquals(mockGameDTO1, list1.getGames().get(0));

        GameListDTO list2 = result.get(1);
        assertEquals("list2", list2.getId());
        assertEquals("To Play", list2.getName());
        assertEquals("Games I want to play", list2.getDescription());
        assertEquals(LocalDateTime.of(2022, 2, 5, 9, 30), list2.getTimeCreated());
        assertEquals(LocalDateTime.of(2022, 2, 6, 11, 45), list2.getTimeModified());
        assertNotNull(list2.getGames());
        assertEquals(1, list2.getGames().size());
        assertEquals(mockGameDTO2, list2.getGames().get(0));

        verify(restTemplate, times(1)).exchange(
                eq(followedListDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verify(restTemplate, times(1)).exchange(
                eq(gameListDatabaseUrl + "/list1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class)
        );

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8092/listToGames/list/list1"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8092/games/game1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class)
        );

        verify(restTemplate, times(1)).exchange(
                eq(gameListDatabaseUrl + "/list2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class)
        );

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8092/listToGames/list/list2"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8092/games/game2"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class)
        );
    }

    @Test
    public void testRequestFollowedLists_HttpClientErrorException() {
        String userId = "user123";

        when(restTemplate.exchange(
                eq(followedListDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<GameListDTO> result = followService.requestFollowedLists(userId);

        assertNull(result);

        verify(restTemplate, times(1)).exchange(
                eq(followedListDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void testRequestFollowedLists_EmptyList() {
        String userId = "user123";
        List<String> followedListsIds = new ArrayList<>();

        ResponseEntity<List> followedListsResponse = new ResponseEntity<>(followedListsIds, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(followedListDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(followedListsResponse);

        List<GameListDTO> result = followService.requestFollowedLists(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restTemplate, times(1)).exchange(
                eq(followedListDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void testRequestFollowedUsers_Success() {
        String userId = "user123";
        List<String> followedUsersIds = Arrays.asList("user456", "user789");

        ResponseEntity<List> followedUsersResponse = new ResponseEntity<>(followedUsersIds, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(followedUsersResponse);

        when(userService.requestUserInfo("user456")).thenReturn(mockUserDTO1);
        when(userService.requestUserInfo("user789")).thenReturn(mockUserDTO2);

        List<UserDTO> result = followService.requestFollowedUsers(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockUserDTO1, result.get(0));
        assertEquals(mockUserDTO2, result.get(1));

        verify(restTemplate, times(1)).exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verify(userService, times(1)).requestUserInfo("user456");
        verify(userService, times(1)).requestUserInfo("user789");
    }

    @Test
    public void testRequestFollowedUsers_HttpClientErrorException() {
        String userId = "user123";

        when(restTemplate.exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<UserDTO> result = followService.requestFollowedUsers(userId);
        assertNull(result);

        verify(restTemplate, times(1)).exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verifyNoInteractions(userService);
    }

    @Test
    public void testRequestFollowedUsers_EmptyList() {
        String userId = "user123";
        List<String> followedUsersIds = new ArrayList<>();

        ResponseEntity<List> followedUsersResponse = new ResponseEntity<>(followedUsersIds, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(followedUsersResponse);

        List<UserDTO> result = followService.requestFollowedUsers(userId);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restTemplate, times(1)).exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verifyNoInteractions(userService);
    }

    @Test
    public void testRequestFollowerUsers_Success() {
        String userId = "user123";
        List<String> followerUsersIds = Arrays.asList("user456", "user789");

        ResponseEntity<List> followerUsersResponse = new ResponseEntity<>(followerUsersIds, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/followers"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(followerUsersResponse);

        when(userService.requestUserInfo("user456")).thenReturn(mockUserDTO1);
        when(userService.requestUserInfo("user789")).thenReturn(mockUserDTO2);

        List<UserDTO> result = followService.requestFollowerUsers(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockUserDTO1, result.get(0));
        assertEquals(mockUserDTO2, result.get(1));

        verify(restTemplate, times(1)).exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/followers"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verify(userService, times(1)).requestUserInfo("user456");
        verify(userService, times(1)).requestUserInfo("user789");
    }

    @Test
    public void testRequestFollowerUsers_HttpClientErrorException() {
        String userId = "user123";

        when(restTemplate.exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/followers"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<UserDTO> result = followService.requestFollowerUsers(userId);

        assertNull(result);

        verify(restTemplate, times(1)).exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/followers"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verifyNoInteractions(userService);
    }

    @Test
    public void testRequestFollowerUsers_EmptyList() {
        String userId = "user123";
        List<String> followerUsersIds = new ArrayList<>();

        ResponseEntity<List> followerUsersResponse = new ResponseEntity<>(followerUsersIds, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/followers"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(followerUsersResponse);

        List<UserDTO> result = followService.requestFollowerUsers(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restTemplate, times(1)).exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/followers"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verifyNoInteractions(userService);
    }

    @Test
    public void testRequestFollowedLists_MalformedData() {
        String userId = "user123";
        List<String> followedListsIds = Arrays.asList("list1");

        ResponseEntity<List> followedListsResponse = new ResponseEntity<>(followedListsIds, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(followedListDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(followedListsResponse);

        ResponseEntity<GameListDTO> list1Response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/list1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class))
        ).thenReturn(list1Response);

        assertThrows(NullPointerException.class, () -> {
            followService.requestFollowedLists(userId);
        });

        verify(restTemplate, times(1)).exchange(
                eq(followedListDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verify(restTemplate, times(1)).exchange(
                eq(gameListDatabaseUrl + "/list1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameListDTO.class)
        );
    }

    @Test
    public void testRequestFollowedUsers_UserServiceException() {
        String userId = "user123";
        List<String> followedUsersIds = Arrays.asList("user456", "user789");

        ResponseEntity<List> followedUsersResponse = new ResponseEntity<>(followedUsersIds, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(followedUsersResponse);

        when(userService.requestUserInfo("user456")).thenReturn(mockUserDTO1);
        when(userService.requestUserInfo("user789")).thenThrow(new RuntimeException("User service failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            followService.requestFollowedUsers(userId);
        });

        assertEquals("User service failed", exception.getMessage());

        verify(restTemplate, times(1)).exchange(
                eq(followedUserDatabaseUrl + "/" + userId + "/following"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        );

        verify(userService, times(1)).requestUserInfo("user456");
        verify(userService, times(1)).requestUserInfo("user789");
    }
}
