package org.gamed.timelineservice.domain;

import org.gamed.timelineservice.models.PostRequestResponseModel;

import java.time.LocalDateTime;
import java.util.Date;

public class Post {

    private String postId;
    private String userId;
    private String postText;
    private LocalDateTime postDate;
    private int likes = 0;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getPostText() {
        return postText;
    }
    public void setPostText(String postText) {
        this.postText = postText;
    }
    public LocalDateTime getPostDate() {
        return postDate;
    }
    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public PostRequestResponseModel toResponseModel(){
        return new PostRequestResponseModel(postDate.toString(),postText,userId,postDate, likes);
    }
}
