package org.gamed.timelineservice.services;

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
    private final RestTemplate restTemplate = new RestTemplate();
    private final String userDatabaseURL = "http://localhost:8090/users";
    private final String followServiceURL = "http://localhost:8090/user/follow";
    private final String reviewServiceURL = "http://localhost:8091/reviews";

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
            System.out.println(response.getBody().getFirst().toString());
            posts.addAll(Objects.requireNonNull(response.getBody()));


        }
        List<PostRequestResponseModel> postList = new ArrayList<>();
        for(ReviewDTO review : posts){
            UserDTO author = this.retrieveUser(review.getUserId());
            PostRequestResponseModel newPost = new PostRequestResponseModel(review.getGameId(),review.getDescription(),author.getName(),review.getTimeCreated().toString(),0);
            postList.add(newPost);
        }

        return postList;
    }

}
