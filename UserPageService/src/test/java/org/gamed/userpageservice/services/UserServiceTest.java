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
public class UserServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    private final String userDatabaseUrl = "http://localhost:8090/users";
    private final String gameListDatabaseUrl = "http://localhost:8092/lists";

    private UserDTO mockUserDTO;
    private GameDTO mockGameDTO;
    private GameListDTO mockGameListDTO;

    @BeforeEach
    public void setUp() {
        mockUserDTO = new UserDTO();

        mockGameDTO = new GameDTO();
        mockGameListDTO = new GameListDTO();
    }

    @Test
    public void testRequestUserInfo_Success() {
        String userId = "user123";
        ResponseEntity<UserDTO> responseEntity = new ResponseEntity<>(mockUserDTO, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(userDatabaseUrl + "/" + userId),
                eq(HttpMethod.GET),
                isNull(),
                eq(UserDTO.class))
        ).thenReturn(responseEntity);

        UserDTO result = userService.requestUserInfo(userId);

        assertNotNull(result);
    }

    @Test
    public void testRequestUserInfo_HttpClientErrorException() {
        String userId = "user123";

        when(restTemplate.exchange(
                eq(userDatabaseUrl + "/" + userId),
                eq(HttpMethod.GET),
                isNull(),
                eq(UserDTO.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        UserDTO result = userService.requestUserInfo(userId);

        assertNull(result);
    }

    @Test
    public void testRequestUserCreatedLists_Success() {
        String userId = "user123";

        List<Map<String, Object>> mockLists = new ArrayList<>();
        Map<String, Object> list1 = new LinkedHashMap<>();
        list1.put("id", "list1");
        list1.put("name", "First List");
        list1.put("description", "Description 1");
        list1.put("timeCreated", "2023-10-01T10:00:00");
        list1.put("timeModified", "2023-10-02T12:00:00");
        mockLists.add(list1);

        ResponseEntity<List> listsResponse = new ResponseEntity<>(new ArrayList<>(mockLists), HttpStatus.OK);

        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/user/" + userId),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(listsResponse);

        List<LinkedHashMap<String, String>> gamesInList = new ArrayList<>();
        LinkedHashMap<String, String> gameRef = new LinkedHashMap<>();
        gameRef.put("game", "game1");
        gamesInList.add(gameRef);

        ResponseEntity<ArrayList<LinkedHashMap<String, String>>> gamesInListResponse =
                new ResponseEntity<>(new ArrayList<>(gamesInList), HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8092/listToGames/list/" + list1.get("id")),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(gamesInListResponse);

        ResponseEntity<GameDTO> gameResponse = new ResponseEntity<>(mockGameDTO, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8092/games/" + gameRef.get("game")),
                eq(HttpMethod.GET),
                isNull(),
                eq(GameDTO.class))
        ).thenReturn(gameResponse);

        List<GameListDTO> result = userService.requestUserCreatedLists(userId);

        assertNotNull(result);
        assertEquals(1, result.size());

        GameListDTO listDTO = result.get(0);
        assertEquals("list1", listDTO.getId());
        assertEquals(userId, listDTO.getUserId());
        assertEquals("First List", listDTO.getName());
        assertEquals("Description 1", listDTO.getDescription());
        assertEquals(LocalDateTime.parse("2023-10-01T10:00:00"), listDTO.getTimeCreated());
        assertEquals(LocalDateTime.parse("2023-10-02T12:00:00"), listDTO.getTimeModified());
        assertNotNull(listDTO.getGames());
        assertEquals(1, listDTO.getGames().size());
    }

    @Test
    public void testRequestUserCreatedLists_HttpClientErrorExceptionOnLists() {
        String userId = "user123";

        when(restTemplate.exchange(
                eq(gameListDatabaseUrl + "/user/" + userId),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<GameListDTO> result = userService.requestUserCreatedLists(userId);

        assertNull(result);
    }
}
