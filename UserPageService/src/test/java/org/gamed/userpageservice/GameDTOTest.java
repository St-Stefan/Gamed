package org.gamed.userpageservice;

import org.gamed.userpageservice.domain.DTOs.GameDTO;
import org.gamed.userpageservice.domain.DTOs.GameListDTO;
import org.gamed.userpageservice.domain.DTOs.PlaytimeDTO;
import org.gamed.userpageservice.domain.DTOs.UserDTO;
import org.gamed.userpageservice.domain.UserPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameDTOTest {
    private UserPage userPage;

    private UserDTO user;
    private List<GameDTO> likedGames;
    private List<GameListDTO> likedLists;
    private List<GameListDTO> followedLists;
    private List<UserDTO> followedUsers;
    private List<UserDTO> followerUsers;
    private List<PlaytimeDTO> playtimes;
    private List<GameListDTO> createdGameLists;

    private UserDTO matei;
    private UserDTO ionescu;
    private UserDTO stoich;

    private GameDTO likedGame1;
    private GameDTO likedGame2;

    private GameListDTO list1;
    private GameListDTO list2;
    private GameListDTO list3;

    private PlaytimeDTO playtime1;
    private PlaytimeDTO playtime2;

    private LocalDateTime date;

    @BeforeEach
    public void setUp() {
        date = LocalDateTime.of(2003, 5, 23, 12, 20, 0);

        matei = new UserDTO("mateiId", "matei", "matei@delft.nl", "hash", false, true, date, LocalDateTime.now());
        ionescu = new UserDTO("ionescuId", "ionescu", "ionescu@delft.nl", "hashI", true, true, date, LocalDateTime.now());
        stoich = new UserDTO("stoichId", "stoich", "stoich@delft.nl", "hashS", true, false, date, LocalDateTime.now());

        likedGame1 = new GameDTO("MC2", "Minecraft 2", "matei", date, "Steam");
        likedGame2 = new GameDTO("CBP2193", "Cyberpunk2193", "ietam", date, "Steam");

        list1 = new GameListDTO("listid1", "mateiId", "matei's list", "matei's liked games", date, LocalDateTime.now());
        list2 =
                new GameListDTO("listid2", "ionescuId", "ione's list", "ione's liked games", date, LocalDateTime.now());
        list3 =
                new GameListDTO("listid3", "stoichId", "stoich's list", "stoich's liked games", date, LocalDateTime.now());

        playtime1 = new PlaytimeDTO("mateiId", "playtimeId1", "MC2Id", 120);
        playtime2 = new PlaytimeDTO("mateiId", "playtimeId2", "CBP2193Id", 10);

        likedGames = new ArrayList<>(List.of(likedGame1, likedGame2));
        likedLists = new ArrayList<>(List.of(list2, list3));
        followedLists = new ArrayList<>(List.of(list2));
        followedUsers = new ArrayList<>(List.of(ionescu, stoich));
        followerUsers = new ArrayList<>(List.of(stoich));
        playtimes = new ArrayList<>(List.of(playtime1, playtime2));
        createdGameLists = new ArrayList<>(List.of(list1));

        userPage = new UserPage(matei, likedGames, likedLists, followedUsers, followerUsers, followedLists, playtimes, createdGameLists);
    }

    @Test
    public void testCreation() {
        assertNotNull(userPage);
        assertEquals(matei, userPage.getUserDTO());
        assertEquals(likedGames, userPage.getLikedGames());
        assertEquals(likedLists, userPage.getLikedLists());
        assertEquals(followedUsers, userPage.getFollowedUsers());
        assertEquals(followerUsers, userPage.getFollowerUsers());
        assertEquals(followedLists, userPage.getFollowedLists());
        assertEquals(playtimes, userPage.getPlaytimes());
        assertEquals(createdGameLists, userPage.getCreatedGameLists());
    }
}
