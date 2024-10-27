package org.gamed.timelineservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gamed.timelineservice.domain.GameDTO;
import org.gamed.timelineservice.domain.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public class PostRequestResponseModel {

    @JsonProperty("title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    @JsonProperty("content")
    private String content;
    @JsonProperty("author")
    private String author;
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("likes")
    private int likes;

    @JsonProperty
    private boolean isList = false;
    @JsonProperty
    private boolean isReview = false;

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


    public PostRequestResponseModel(String title, String content, String author, LocalDateTime timestamp, int likes) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.likes = likes;
    }


}
