package org.gamed.timelineservice.services;

import org.gamed.timelineservice.adapters.GameListDTOToPostAdapter;
import org.gamed.timelineservice.domain.GameDTO;
import org.gamed.timelineservice.domain.GameListDTO;
import org.gamed.timelineservice.domain.ReviewDTO;
import org.gamed.timelineservice.domain.UserDTO;
import org.gamed.timelineservice.models.PostRequestResponseModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserRetrievalService {
    private final static RestTemplate restTemplate = new RestTemplate();
    private final static String userDatabaseURL = "http://localhost:8090/users";
    private final static String followServiceURL = "http://localhost:8090/user/followed-users";
    private final static String reviewServiceURL = "http://localhost:8091/reviews";
    private final static String gameServiceURL = "http://localhost:8092/games/";

    public UserDTO retrieveUser(String UID) {
        ResponseEntity<UserDTO> response;

        try{
            response = restTemplate.getForEntity(userDatabaseURL+"/"+UID, UserDTO.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }

    public List<String> retrieveFollowList(String UID) {
        ResponseEntity<List<String>> response;

        try{
            response = restTemplate.exchange(followServiceURL+"/"+UID + "/" + "following", HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {} );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }

    public List<PostRequestResponseModel> retrieveAllPosts(List<String> followList) {

        List<ReviewDTO> posts = new ArrayList<>();

        for(String uid : followList){
            ResponseEntity<List<ReviewDTO>> response;
            try{
                response = restTemplate.exchange(reviewServiceURL+"/user/"+uid, HttpMethod.GET, null, new ParameterizedTypeReference<List<ReviewDTO>>() {} );
            } catch (HttpClientErrorException e) {
                throw new RuntimeException(e);
            }
            posts.addAll(Objects.requireNonNull(response.getBody()));


        }
        List<PostRequestResponseModel> postList = new ArrayList<>();
        for(ReviewDTO review : posts){
            UserDTO author = this.retrieveUser(review.getUserId());
            GameDTO game = this.retrieveGame(review.getGameId());
            List<GameDTO> reviewed = new ArrayList<>();
            reviewed.add(game);

            PostRequestResponseModel newPost = new PostRequestResponseModel(review.getGameId(),review.getDescription(),author.getName(),review.getTimeCreated(),0);
            newPost.setUser(author);
            newPost.setGames(reviewed);
            newPost.setList(false);
            newPost.setReview(true);
            postList.add(newPost);
        }

        return postList;
    }

    public List<PostRequestResponseModel> retrieveAllLists(List<String> followList) {

        List<GameListDTO> posts = new ArrayList<>();

        for(String uid : followList){
            List<GameListDTO> allForUser = ListRetrievalService.requestUserCreatedLists(uid);
            if(allForUser!=null)
                posts.addAll(allForUser);
        }
        List<PostRequestResponseModel> postList = new ArrayList<>();
        for(GameListDTO list : posts){
            UserDTO author = this.retrieveUser(list.getUserId());
            PostRequestResponseModel postFromList = GameListDTOToPostAdapter.convert(list,author);
            postList.add(postFromList);
        }
        return postList;
    }



    public static GameDTO retrieveGame(String gameID) {
        ResponseEntity<GameDTO> response;

        try{
            response = restTemplate.getForEntity(gameServiceURL+"/"+gameID, GameDTO.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }


}
