package org.gamed.userpageservice.services;

import org.gamed.userpageservice.DTOs.*;
import org.gamed.userpageservice.DTOs.UserPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserPageServiceTest {

    @Mock
    private FollowService followService;

    @Mock
    private UserService userService;

    @Mock
    private LikeService likeService;

    @Mock
    private PlaytimeService playtimeService;

    @InjectMocks
    private UserPageService userPageService;

    private UserDTO mockUserDTO;
    private LikeDTO mockLikeGameDTO;
    private LikeDTO mockLikeListDTO;
    private GameDTO mockLikedGameDTO;
    private GameListDTO mockLikedGameListDTO;
    private UserDTO mockFollowedUserDTO1;
    private UserDTO mockFollowedUserDTO2;
    private UserDTO mockFollowerUserDTO1;
    private UserDTO mockFollowerUserDTO2;
    private GameListDTO mockFollowedListDTO;
    private PlaytimeDTO mockPlaytimeDTO1;
    private PlaytimeDTO mockPlaytimeDTO2;
    private GameListDTO mockCreatedGameListDTO;

    @BeforeEach
    public void setUp() {
        mockUserDTO = new UserDTO();
        mockUserDTO.setId("user123");
        mockUserDTO.setName("TestUser");
        mockUserDTO.setEmail("testuser@example.com");

        mockLikeGameDTO = new LikeDTO();
        mockLikeGameDTO.setId("like1");
        mockLikeGameDTO.setType("Game");
        mockLikeGameDTO.setItemId("game1");

        mockLikeListDTO = new LikeDTO();
        mockLikeListDTO.setId("like2");
        mockLikeListDTO.setType("List");
        mockLikeListDTO.setItemId("list1");

        mockLikedGameDTO = new GameDTO(
                "game1",
                "Awesome Game",
                "GameDev Studios",
                LocalDateTime.of(2020, 5, 20, 0, 0),
                "PC, PS4"
        );

        mockLikedGameListDTO = new GameListDTO();
        mockLikedGameListDTO.setId("list1");
        mockLikedGameListDTO.setUserId("user123");
        mockLikedGameListDTO.setName("Top Picks");
        mockLikedGameListDTO.setDescription("My top picked games");
        mockLikedGameListDTO.setTimeCreated(LocalDateTime.of(2021, 6, 15, 10, 0));
        mockLikedGameListDTO.setTimeModified(LocalDateTime.of(2021, 6, 20, 12, 0));
        mockLikedGameListDTO.setGames(Arrays.asList(mockLikedGameDTO));

        mockFollowedUserDTO1 = new UserDTO();
        mockFollowedUserDTO1.setId("user456");
        mockFollowedUserDTO1.setName("FollowerOne");
        mockFollowedUserDTO1.setEmail("followerone@example.com");

        mockFollowedUserDTO2 = new UserDTO();
        mockFollowedUserDTO2.setId("user789");
        mockFollowedUserDTO2.setName("FollowerTwo");
        mockFollowedUserDTO2.setEmail("followertwo@example.com");

        mockFollowerUserDTO1 = new UserDTO();
        mockFollowerUserDTO1.setId("user321");
        mockFollowerUserDTO1.setName("FollowerThree");
        mockFollowerUserDTO1.setEmail("followerthree@example.com");

        mockFollowerUserDTO2 = new UserDTO();
        mockFollowerUserDTO2.setId("user654");
        mockFollowerUserDTO2.setName("FollowerFour");
        mockFollowerUserDTO2.setEmail("followerfour@example.com");

        mockFollowedListDTO = new GameListDTO();
        mockFollowedListDTO.setId("list2");
        mockFollowedListDTO.setUserId("user123");
        mockFollowedListDTO.setName("Upcoming Games");
        mockFollowedListDTO.setDescription("Games I plan to play");
        mockFollowedListDTO.setTimeCreated(LocalDateTime.of(2021, 7, 10, 9, 30));
        mockFollowedListDTO.setTimeModified(LocalDateTime.of(2021, 7, 12, 11, 45));

        mockPlaytimeDTO1 = new PlaytimeDTO(
                "user123",
                "playtime1",
                "game1",
                120,
                LocalDateTime.of(2021, 8, 1, 14, 0),
                LocalDateTime.of(2021, 8, 2, 16, 0)
        );

        mockPlaytimeDTO2 = new PlaytimeDTO(
                "user123",
                "playtime2",
                "game2",
                90,
                LocalDateTime.of(2021, 9, 1, 10, 30),
                LocalDateTime.of(2021, 9, 2, 12, 45)
        );

        mockCreatedGameListDTO = new GameListDTO();
        mockCreatedGameListDTO.setId("list3");
        mockCreatedGameListDTO.setUserId("user123");
        mockCreatedGameListDTO.setName("Recently Played");
        mockCreatedGameListDTO.setDescription("Games I've recently played");
        mockCreatedGameListDTO.setTimeCreated(LocalDateTime.of(2021, 10, 5, 8, 15));
        mockCreatedGameListDTO.setTimeModified(LocalDateTime.of(2021, 10, 6, 10, 30));
        mockCreatedGameListDTO.setGames(new ArrayList<>()); // Assume games are fetched separately
    }

    @Test
    public void testRequestUserPage_Success() {
        String userId = "user123";

        when(userService.requestUserInfo(userId)).thenReturn(mockUserDTO);

        List<LikeDTO> mockLikes = Arrays.asList(mockLikeGameDTO, mockLikeListDTO);
        when(likeService.requestUserLikeInfo(userId)).thenReturn(mockLikes);
        when(likeService.requestLikedGames(Arrays.asList("game1"))).thenReturn(Arrays.asList(mockLikedGameDTO));
        when(likeService.requestLikedLists(Arrays.asList("list1"))).thenReturn(Arrays.asList(mockLikedGameListDTO));

        when(followService.requestFollowedUsers(userId)).thenReturn(Arrays.asList(mockFollowedUserDTO1, mockFollowedUserDTO2));
        when(followService.requestFollowerUsers(userId)).thenReturn(Arrays.asList(mockFollowerUserDTO1, mockFollowerUserDTO2));
        when(followService.requestFollowedLists(userId)).thenReturn(Arrays.asList(mockFollowedListDTO));

        when(playtimeService.requestPlaytime(userId)).thenReturn(Arrays.asList(mockPlaytimeDTO1, mockPlaytimeDTO2));

        when(userService.requestUserCreatedLists(userId)).thenReturn(Arrays.asList(mockCreatedGameListDTO));

        UserPage result = userPageService.requestUserPage(userId);

        assertNotNull(result, "UserPage should not be null");
        assertEquals(mockUserDTO, result.getUserDTO(), "UserDTO should match");
        assertEquals(1, result.getLikedGames().size(), "There should be one liked game");
        assertEquals(mockLikedGameDTO, result.getLikedGames().get(0), "Liked GameDTO should match");
        assertEquals(1, result.getLikedLists().size(), "There should be one liked list");
        assertEquals(mockLikedGameListDTO, result.getLikedLists().get(0), "Liked GameListDTO should match");
        assertEquals(2, result.getFollowedUsers().size(), "There should be two followed users");
        assertTrue(result.getFollowedUsers().contains(mockFollowedUserDTO1), "FollowedUserDTO1 should be present");
        assertTrue(result.getFollowedUsers().contains(mockFollowedUserDTO2), "FollowedUserDTO2 should be present");
        assertEquals(2, result.getFollowerUsers().size(), "There should be two follower users");
        assertTrue(result.getFollowerUsers().contains(mockFollowerUserDTO1), "FollowerUserDTO1 should be present");
        assertTrue(result.getFollowerUsers().contains(mockFollowerUserDTO2), "FollowerUserDTO2 should be present");
        assertEquals(1, result.getFollowedLists().size(), "There should be one followed list");
        assertEquals(mockFollowedListDTO, result.getFollowedLists().get(0), "Followed GameListDTO should match");
        assertEquals(1, result.getCreatedGameLists().size(), "There should be one created game list");
        assertEquals(mockCreatedGameListDTO, result.getCreatedGameLists().get(0), "Created GameListDTO should match");

        verify(userService, times(1)).requestUserInfo(userId);
        verify(likeService, times(1)).requestUserLikeInfo(userId);
        verify(likeService, times(1)).requestLikedGames(Arrays.asList("game1"));
        verify(likeService, times(1)).requestLikedLists(Arrays.asList("list1"));
        verify(followService, times(1)).requestFollowedUsers(userId);
        verify(followService, times(1)).requestFollowerUsers(userId);
        verify(followService, times(1)).requestFollowedLists(userId);
        verify(playtimeService, times(1)).requestPlaytime(userId);
        verify(userService, times(1)).requestUserCreatedLists(userId);
    }

    @Test
    public void testRequestUserPage_UserNotFound() {
        String userId = "user123";

        when(userService.requestUserInfo(userId)).thenReturn(null);

        UserPage result = userPageService.requestUserPage(userId);

        assertNull(result, "UserPage should be null when user is not found");

        verify(userService, times(1)).requestUserInfo(userId);
        verifyNoInteractions(likeService);
        verifyNoInteractions(followService);
        verifyNoInteractions(playtimeService);
    }

    @Test
    public void testRequestUserPage_LikeServiceReturnsNull() {
        String userId = "user123";

        when(userService.requestUserInfo(userId)).thenReturn(mockUserDTO);

        when(likeService.requestUserLikeInfo(userId)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            userPageService.requestUserPage(userId);
        }, "Expected NullPointerException due to null liked items");

        verify(userService, times(1)).requestUserInfo(userId);
        verify(likeService, times(1)).requestUserLikeInfo(userId);
        verifyNoMoreInteractions(likeService);
        verifyNoInteractions(followService);
        verifyNoInteractions(playtimeService);
    }

    @Test
    public void testRequestUserPage_DependentServiceThrowsException() {
        String userId = "user123";

        when(userService.requestUserInfo(userId)).thenReturn(mockUserDTO);

        List<LikeDTO> mockLikes = Arrays.asList(mockLikeGameDTO, mockLikeListDTO);
        when(likeService.requestUserLikeInfo(userId)).thenReturn(mockLikes);
        when(likeService.requestLikedGames(Arrays.asList("game1"))).thenReturn(Arrays.asList(mockLikedGameDTO));
        when(likeService.requestLikedLists(Arrays.asList("list1"))).thenReturn(Arrays.asList(mockLikedGameListDTO));

        when(followService.requestFollowedUsers(userId)).thenReturn(Arrays.asList(mockFollowedUserDTO1, mockFollowedUserDTO2));
        when(followService.requestFollowerUsers(userId)).thenReturn(Arrays.asList(mockFollowerUserDTO1, mockFollowerUserDTO2));
        when(followService.requestFollowedLists(userId)).thenReturn(Arrays.asList(mockFollowedListDTO));

        when(playtimeService.requestPlaytime(userId)).thenThrow(new RuntimeException("Playtime service failure"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userPageService.requestUserPage(userId);
        }, "Expected RuntimeException due to PlaytimeService failure");

        assertEquals("Playtime service failure", exception.getMessage());

        verify(userService, times(1)).requestUserInfo(userId);
        verify(likeService, times(1)).requestUserLikeInfo(userId);
        verify(likeService, times(1)).requestLikedGames(Arrays.asList("game1"));
        verify(likeService, times(1)).requestLikedLists(Arrays.asList("list1"));
        verify(followService, times(1)).requestFollowedUsers(userId);
        verify(followService, times(1)).requestFollowerUsers(userId);
        verify(followService, times(1)).requestFollowedLists(userId);
        verify(playtimeService, times(1)).requestPlaytime(userId);
    }

    @Test
    public void testRequestUserPage_LikeServiceReturnsEmptyLists() {
        String userId = "user123";

        when(userService.requestUserInfo(userId)).thenReturn(mockUserDTO);

        List<LikeDTO> mockLikes = new ArrayList<>();
        when(likeService.requestUserLikeInfo(userId)).thenReturn(mockLikes);

        when(followService.requestFollowedUsers(userId)).thenReturn(Arrays.asList(mockFollowedUserDTO1, mockFollowedUserDTO2));
        when(followService.requestFollowerUsers(userId)).thenReturn(Arrays.asList(mockFollowerUserDTO1, mockFollowerUserDTO2));
        when(followService.requestFollowedLists(userId)).thenReturn(Arrays.asList(mockFollowedListDTO));

        when(playtimeService.requestPlaytime(userId)).thenReturn(Arrays.asList(mockPlaytimeDTO1, mockPlaytimeDTO2));

        when(userService.requestUserCreatedLists(userId)).thenReturn(Arrays.asList(mockCreatedGameListDTO));

        UserPage result = userPageService.requestUserPage(userId);

        assertNotNull(result, "UserPage should not be null");
        assertEquals(mockUserDTO, result.getUserDTO(), "UserDTO should match");
        assertTrue(result.getLikedGames().isEmpty(), "There should be no liked games");
        assertTrue(result.getLikedLists().isEmpty(), "There should be no liked lists");
        assertEquals(2, result.getFollowedUsers().size(), "There should be two followed users");
        assertTrue(result.getFollowedUsers().contains(mockFollowedUserDTO1), "FollowedUserDTO1 should be present");
        assertTrue(result.getFollowedUsers().contains(mockFollowedUserDTO2), "FollowedUserDTO2 should be present");
        assertEquals(2, result.getFollowerUsers().size(), "There should be two follower users");
        assertTrue(result.getFollowerUsers().contains(mockFollowerUserDTO1), "FollowerUserDTO1 should be present");
        assertTrue(result.getFollowerUsers().contains(mockFollowerUserDTO2), "FollowerUserDTO2 should be present");
        assertEquals(1, result.getFollowedLists().size(), "There should be one followed list");
        assertEquals(mockFollowedListDTO, result.getFollowedLists().get(0), "Followed GameListDTO should match");
        assertEquals(1, result.getCreatedGameLists().size(), "There should be one created game list");
        assertEquals(mockCreatedGameListDTO, result.getCreatedGameLists().get(0), "Created GameListDTO should match");
    }

    @Test
    public void testRequestUserPage_PlaytimeServiceReturnsEmptyList() {
        String userId = "user123";

        when(userService.requestUserInfo(userId)).thenReturn(mockUserDTO);

        List<LikeDTO> mockLikes = Arrays.asList(mockLikeGameDTO, mockLikeListDTO);
        when(likeService.requestUserLikeInfo(userId)).thenReturn(mockLikes);
        when(likeService.requestLikedGames(Arrays.asList("game1"))).thenReturn(Arrays.asList(mockLikedGameDTO));
        when(likeService.requestLikedLists(Arrays.asList("list1"))).thenReturn(Arrays.asList(mockLikedGameListDTO));

        when(followService.requestFollowedUsers(userId)).thenReturn(Arrays.asList(mockFollowedUserDTO1, mockFollowedUserDTO2));
        when(followService.requestFollowerUsers(userId)).thenReturn(Arrays.asList(mockFollowerUserDTO1, mockFollowerUserDTO2));
        when(followService.requestFollowedLists(userId)).thenReturn(Arrays.asList(mockFollowedListDTO));

        when(playtimeService.requestPlaytime(userId)).thenReturn(new ArrayList<>());

        when(userService.requestUserCreatedLists(userId)).thenReturn(Arrays.asList(mockCreatedGameListDTO));

        UserPage result = userPageService.requestUserPage(userId);

        assertNotNull(result, "UserPage should not be null");
        assertEquals(mockUserDTO, result.getUserDTO(), "UserDTO should match");
        assertEquals(1, result.getLikedGames().size(), "There should be one liked game");
        assertEquals(mockLikedGameDTO, result.getLikedGames().get(0), "Liked GameDTO should match");
        assertEquals(1, result.getLikedLists().size(), "There should be one liked list");
        assertEquals(mockLikedGameListDTO, result.getLikedLists().get(0), "Liked GameListDTO should match");
        assertEquals(2, result.getFollowedUsers().size(), "There should be two followed users");
        assertTrue(result.getFollowedUsers().contains(mockFollowedUserDTO1), "FollowedUserDTO1 should be present");
        assertTrue(result.getFollowedUsers().contains(mockFollowedUserDTO2), "FollowedUserDTO2 should be present");
        assertEquals(2, result.getFollowerUsers().size(), "There should be two follower users");
        assertTrue(result.getFollowerUsers().contains(mockFollowerUserDTO1), "FollowerUserDTO1 should be present");
        assertTrue(result.getFollowerUsers().contains(mockFollowerUserDTO2), "FollowerUserDTO2 should be present");
        assertEquals(1, result.getFollowedLists().size(), "There should be one followed list");
        assertEquals(mockFollowedListDTO, result.getFollowedLists().get(0), "Followed GameListDTO should match");
        assertEquals(1, result.getCreatedGameLists().size(), "There should be one created game list");
        assertEquals(mockCreatedGameListDTO, result.getCreatedGameLists().get(0), "Created GameListDTO should match");
    }
}
