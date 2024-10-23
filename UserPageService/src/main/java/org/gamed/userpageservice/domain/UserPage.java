package org.gamed.userpageservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.gamed.userpageservice.domain.DTOs.*;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class UserPage {
    private UserDTO userDTO;

    private List<GameDTO> likedGames;

    private List<GameListDTO> likedLists;

    private List<GameListDTO> followedLists;

    private List<UserDTO> followedUsers;

    private List<UserDTO> followerUsers;

    private List<PlaytimeDTO> playtime;

    public UserPage(UserDTO userDTO, List<GameDTO> likedGames, List<GameListDTO> likedLists,
                    List<UserDTO> followedUsers, List<UserDTO> followerUsers, List<GameListDTO> followedLists,
                    List<PlaytimeDTO> playtime) {
        this.userDTO = userDTO;
        this.likedGames = likedGames;
        this.likedLists = likedLists;
        this.followedUsers = followedUsers;
        this.followerUsers = followerUsers;
        this.followedLists = followedLists;
        this.playtime = playtime;
    }
}
