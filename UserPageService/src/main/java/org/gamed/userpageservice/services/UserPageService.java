package org.gamed.userpageservice.services;

import org.gamed.userpageservice.DTOs.*;
import org.gamed.userpageservice.DTOs.UserPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPageService {
    private final FollowService followService;
    private final UserService userService;
    private final LikeService likeService;
    private final PlaytimeService playtimeService;

    public UserPageService(FollowService followService, UserService userService, LikeService likeService, PlaytimeService playtimeService) {
        this.followService = followService;
        this.userService = userService;
        this.likeService = likeService;
        this.playtimeService = playtimeService;
    }

    public UserPage requestUserPage(String userId) {
        UserDTO userDTO = userService.requestUserInfo(userId);

        if (userDTO == null) {
            return null;
        }

        List<LikeDTO> userLikeInfo = likeService.requestUserLikeInfo(userId);

        List<String> likedGamesIds = new ArrayList<>();
        List<String> likedListsIds = new ArrayList<>();


        for(LikeDTO entry : userLikeInfo) {
            String type = entry.getType();
            switch (type) {
                case "Game":
                    likedGamesIds.add(entry.getItemId());
                    break;
                case "List":
                    likedListsIds.add(entry.getItemId());
                    break;
            }
        }

        List<GameDTO> gameDTO = new ArrayList<>();
        if (!likedGamesIds.isEmpty()) {
            gameDTO = likeService.requestLikedGames(likedGamesIds);
        }

        List<GameListDTO> gameListDTO = new ArrayList<>();
        if (!likedListsIds.isEmpty()) {
            gameListDTO = likeService.requestLikedLists(likedListsIds);
        }

        List<UserDTO> followedUsers = followService.requestFollowedUsers(userId);
        List<UserDTO> followerUsers = followService.requestFollowerUsers(userId);

        List<GameListDTO> followedLists = followService.requestFollowedLists(userId);

        List<PlaytimeDTO> playtime = playtimeService.requestPlaytime(userId);

        List<GameListDTO> createdGameLists = userService.requestUserCreatedLists(userId);

        return new UserPage(userDTO, gameDTO, gameListDTO, followedUsers, followerUsers, followedLists, playtime,
                            createdGameLists);
    }
}
