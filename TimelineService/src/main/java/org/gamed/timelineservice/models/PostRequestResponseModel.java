package org.gamed.timelineservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gamed.timelineservice.domain.GameDTO;
import org.gamed.timelineservice.domain.UserDTO;

import java.util.List;

public class PostRequestResponseModel {

    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("author")
    private String author;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("likes")
    private int likes;

    @JsonProperty
    private boolean isList;
    @JsonProperty
    private boolean isReview;

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @JsonProperty
    private List<GameDTO> games;
    @JsonProperty
    private UserDTO user;


    public PostRequestResponseModel(String title, String content, String author, String timestamp, int likes) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.likes = likes;
    }


}
