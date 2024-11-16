package org.gamed.timelineservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public PostRequestResponseModel(String title, String content, String author, String timestamp, int likes) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.likes = likes;
    }
}
