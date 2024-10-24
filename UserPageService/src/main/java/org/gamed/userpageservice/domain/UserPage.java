package org.gamed.userpageservice.domain;

import lombok.Getter;
import lombok.ToString;
import org.gamed.userpageservice.domain.DTOs.GameDTO;
import org.gamed.userpageservice.domain.DTOs.GameListDTO;
import org.gamed.userpageservice.domain.DTOs.PlaytimeDTO;
import org.gamed.userpageservice.domain.DTOs.UserDTO;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPage userPage = (UserPage) o;
        return Objects.equals(userDTO, userPage.userDTO) && Objects.equals(likedGames, userPage.likedGames) && Objects.equals(likedLists, userPage.likedLists) && Objects.equals(followedLists, userPage.followedLists) && Objects.equals(followedUsers, userPage.followedUsers) && Objects.equals(followerUsers, userPage.followerUsers) && Objects.equals(playtimes, userPage.playtimes) && Objects.equals(createdGameLists, userPage.createdGameLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDTO, likedGames, likedLists, followedLists, followedUsers, followerUsers, playtimes, createdGameLists);
    }
}
