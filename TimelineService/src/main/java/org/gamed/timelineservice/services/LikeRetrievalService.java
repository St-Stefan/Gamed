package org.gamed.timelineservice.services;

import org.gamed.timelineservice.domain.GameDTO;
import org.gamed.timelineservice.domain.GameListDTO;
import org.gamed.timelineservice.domain.LikeDTO;
import org.gamed.timelineservice.domain.UserDTO;
import org.gamed.timelineservice.models.PostRequestResponseModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LikeRetrievalService {

    private static final RestTemplate restTemplate = new RestTemplate();
    private final UserRetrievalService userRetrievalService;

    public LikeRetrievalService(UserRetrievalService userRetrievalService) {
        this.userRetrievalService = userRetrievalService;
    }

    public static List<LikeDTO> requestLikes(String userId) {
        String likeDatabaseURL = "http://localhost:8090";
        try {
            ResponseEntity<List<LikeDTO>> response = restTemplate.exchange(
                    likeDatabaseURL + "/user/likes/" + userId + "/liked-items",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LikeDTO>>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error retrieving liked items", e);
        }
    }

    public PostRequestResponseModel convertLikeDtoToPostRequestResponseModel(LikeDTO likeDTO) {
        if (likeDTO == null) {
            throw new IllegalArgumentException("LikeDTO cannot be null");
        }

        // Retrieve user information
        UserDTO user = userRetrievalService.retrieveUser(likeDTO.getUser());

        // Initialize variables
        String title = null;
        String content = null;
        String author = user.getName();
        LocalDateTime timestamp = likeDTO.getTime();
        int likes = 0;
        boolean isList = false;
        boolean isReview = false;
        boolean isListLike = false;
        boolean isGameLike = false;
        List<GameDTO> games = null;

        // Retrieve the liked item based on type
        switch (likeDTO.getType().toLowerCase()) {
            case "game":
                GameDTO game = UserRetrievalService.retrieveGame(likeDTO.getItemId());
                if (game != null) {
                    title = game.getName();
                    content = user.getName() + " likes the game:";
                    likes = 0;
                    isReview = false;
                    isGameLike = true;
                    // Wrap the game in a list
                    games = List.of(game);
                }
                break;

            case "list":
                GameListDTO list = ListRetrievalService.retrieveList(likeDTO.getItemId());
                if (list != null) {
                    title = list.getName();
                    content = user.getName() + " has liked the list " +list.getDescription();
                    likes = 0;
                    isList = false;
                    isListLike = true;
                    games = list.getGames();
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown type: " + likeDTO.getType());
        }

        // Create and populate the PostRequestResponseModel
        PostRequestResponseModel responseModel = new PostRequestResponseModel(title, content, author, timestamp, likes);
        responseModel.setUser(user);
        responseModel.setGames(games);
        responseModel.setListLike(isListLike);
        responseModel.setGameLike(isGameLike);
        return responseModel;
    }

    public List<PostRequestResponseModel> getLikePosts (String userId){
        List<LikeDTO> likes = requestLikes(userId);
        if(!likes.isEmpty())System.out.println(likes.getFirst().toString());
        List<PostRequestResponseModel> response = likes.stream().map(this::convertLikeDtoToPostRequestResponseModel).toList();
        return response;
    }

    public List<PostRequestResponseModel> getFriendsLikePost (List<String> userIds){
        List<PostRequestResponseModel> returnLikes = new ArrayList<>();
        for(String userId : userIds){
            List<LikeDTO> likes = requestLikes(userId);
            List<PostRequestResponseModel> friendsLikePosts = getLikePosts(userId);
            returnLikes.addAll(friendsLikePosts);
        }
        return returnLikes;
    }
}
