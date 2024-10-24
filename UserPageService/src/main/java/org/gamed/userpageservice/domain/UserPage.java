package org.gamed.userpageservice.domain;

import lombok.Getter;
import lombok.ToString;
import org.gamed.userpageservice.domain.DTOs.GameDTO;
import org.gamed.userpageservice.domain.DTOs.GameListDTO;
import org.gamed.userpageservice.domain.DTOs.PlaytimeDTO;
import org.gamed.userpageservice.domain.DTOs.UserDTO;

import java.util.List;

@Getter
@ToString
public class UserPage {
    private UserDTO userDTO;

    private List<GameDTO> likedGames;

    private List<GameListDTO> likedLists;

    private List<GameListDTO> followedLists;

    private List<UserDTO> followedUsers;

    private List<UserDTO> followerUsers;

    private List<PlaytimeDTO> playtimes;

    private List<GameListDTO> createdGameLists;

    public UserPage(UserDTO userDTO, List<GameDTO> likedGames, List<GameListDTO> likedLists,
                    List<UserDTO> followedUsers, List<UserDTO> followerUsers, List<GameListDTO> followedLists,
                    List<PlaytimeDTO> playtimes, List<GameListDTO> createdGameLists) {
        this.userDTO = userDTO;
        this.likedGames = likedGames;
        this.likedLists = likedLists;
        this.followedUsers = followedUsers;
        this.followerUsers = followerUsers;
        this.followedLists = followedLists;
        this.playtimes = playtimes;
        this.createdGameLists = createdGameLists;
    }
}
