//package org.gamed.userpageservice.domain;
//
//import org.gamed.userpageservice.domain.DTOs.*;
//import org.gamed.userpageservice.services.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.gamed.userpageservice.services.UserPageService.requestUserPage;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class UserPageControllerTests {
//    @MockBean
//    private UserService userService;
//    @MockBean
//    private FollowService followService;
//    @MockBean
//    private LikeService likeService;
//    @MockBean
//    private PlaytimeService playtimeService;
//
//    @Mock
//    private RestTemplate mockRestTemplate;
//
//    @InjectMocks
//    private UserPageService userPageService;
//
//    private UserPage userPage;
//
//    private MockRestTemplate restTemplateHelper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        userPage = createUserPage();
//
//        restTemplateHelper = new MockRestTemplate(mockRestTemplate);
//        restTemplateHelper.setUpMocks(userPage);
//
//        userService = new UserService(mockRestTemplate);
//        likeService = new LikeService(mockRestTemplate);
//        followService = new FollowService(mockRestTemplate);
//        playtimeService = new PlaytimeService(mockRestTemplate);
//    }
//
//    @Test
//    public void testRequestUserPage() {
//        String userId = "mateiId";
//
//        UserPage result = requestUserPage(userId);
//
//        assertNotNull(result);
//        assertNotNull(result.getUserDTO());
//        assertNotNull(result.getFollowedUsers());
//        assertNotNull(result.getFollowerUsers());
//        assertNotNull(result.getFollowedLists());
//        assertNotNull(result.getPlaytimes());
//        assertNotNull(result.getLikedGames());
//        assertNotNull(result.getLikedLists());
//        assertNotNull(result.getCreatedGameLists());
//
//        assertEquals(userPage, result);
//    }
//
//    private UserPage createUserPage() {
//        LocalDateTime date = LocalDateTime.of(2003, 5, 23, 12, 20, 0);
//
//        UserDTO matei = new UserDTO("mateiId", "matei", "matei@delft.nl", "hash", false, true, date, LocalDateTime.now());
//        UserDTO ionescu = new UserDTO("ionescuId", "ionescu", "ionescu@delft.nl", "hashI", true, true, date, LocalDateTime.now());
//        UserDTO stoich = new UserDTO("stoichId", "stoich", "stoich@delft.nl", "hashS", true, false, date, LocalDateTime.now());
//
//        GameDTO likedGame1 = new GameDTO("game1Id", "game1", "matei", date, "Steam");
//        GameDTO likedGame2 = new GameDTO("game2Id", "game2", "ietam", date, "Steam");
//
//        List<GameDTO> gamesList1 = new ArrayList<>();
//        List<GameDTO> gamesList2 = new ArrayList<>();
//        List<GameDTO> gamesList3 = new ArrayList<>();
//
//        gamesList3.add(likedGame1);
//        gamesList2.add(likedGame2);
//        gamesList1.addAll(List.of(likedGame1, likedGame2));
//
//        GameListDTO list1 = new GameListDTO("listid1", "mateiId", "matei's list", "matei's liked games", date, LocalDateTime.now(), gamesList1);
//        GameListDTO list2 = new GameListDTO("listid2", "ionescuId", "ione's list", "ione's liked games", date, LocalDateTime.now(), gamesList2);
//        GameListDTO list3 = new GameListDTO("listid3", "stoichId", "stoich's list", "stoich's liked games", date, LocalDateTime.now(), gamesList3);
//
//        PlaytimeDTO playtime1 = new PlaytimeDTO("mateiId", "playtimeId1", "game1Id", 120);
//        PlaytimeDTO playtime2 = new PlaytimeDTO("mateiId", "playtimeId2", "game2Id", 10);
//
//        List<GameDTO> likedGames = new ArrayList<>(List.of(likedGame1, likedGame2));
//        List<GameListDTO> likedLists = new ArrayList<>(List.of(list2, list3));
//        List<GameListDTO> followedLists = new ArrayList<>(List.of(list2));
//        List<UserDTO> followedUsers = new ArrayList<>(List.of(ionescu, stoich));
//        List<UserDTO> followerUsers = new ArrayList<>(List.of(stoich));
//        List<PlaytimeDTO> playtimes = new ArrayList<>(List.of(playtime1, playtime2));
//        List<GameListDTO> createdGameLists = new ArrayList<>(List.of(list1));
//
//        return new UserPage(matei, likedGames, likedLists, followedUsers, followerUsers, followedLists, playtimes, createdGameLists);
//    }
//}
