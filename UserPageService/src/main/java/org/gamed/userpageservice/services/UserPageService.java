package org.gamed.userpageservice.services;

import lombok.NoArgsConstructor;
import org.gamed.userpageservice.domain.DTOs.GameDTO;
import org.gamed.userpageservice.domain.DTOs.GameListDTO;
import org.gamed.userpageservice.domain.DTOs.PlaytimeDTO;
import org.gamed.userpageservice.domain.DTOs.UserDTO;
import org.gamed.userpageservice.domain.UserPage;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.gamed.userpageservice.services.FollowService.*;
import static org.gamed.userpageservice.services.LikeService.*;
import static org.gamed.userpageservice.services.PlaytimeService.requestPlaytime;
import static org.gamed.userpageservice.services.UserService.requestUserInfo;

@Service
@NoArgsConstructor
public class UserPageService {

    public static UserPage requestUserPage(String userId) {
        UserDTO userDTO = requestUserInfo(userId);

        if (userDTO == null) {
            return null;
        }

        List<LinkedHashMap<String, String>> userLikeInfo = requestUserLikeInfo(userId);

        List<String> likedGamesIds = new ArrayList<>();
        List<String> likedListsIds = new ArrayList<>();


        for(LinkedHashMap<String, String> entry : userLikeInfo) {
            String key = entry.sequencedKeySet().getFirst();
            switch (entry.get(key)) {
                    case "Game":
                        likedGamesIds.add(key);
                        break;
                    case "List":
                        likedListsIds.add(key);
                        break;
                }
        }

        List<GameDTO> gameDTO = new ArrayList<>();
        if (!likedGamesIds.isEmpty()) {
            gameDTO = requestLikedGames(likedGamesIds);
        }

        List<GameListDTO> gameListDTO = new ArrayList<>();
        if (!likedListsIds.isEmpty()) {
            gameListDTO = requestLikedLists(likedListsIds);
        }

        List<UserDTO> followedUsers = requestFollowedUsers(userId);
        List<UserDTO> followerUsers = requestFollowerUsers(userId);

        List<GameListDTO> followedLists = requestFollowedLists(userId);

        List<PlaytimeDTO> playtime = requestPlaytime(userId);

        return new UserPage(userDTO, gameDTO, gameListDTO, followedUsers, followerUsers, followedLists, playtime);
    }
}
