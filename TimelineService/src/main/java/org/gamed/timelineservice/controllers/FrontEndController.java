package org.gamed.timelineservice.controllers;

import org.gamed.timelineservice.domain.GameListDTO;
import org.gamed.timelineservice.domain.Post;
import org.gamed.timelineservice.domain.UserDTO;
import org.gamed.timelineservice.models.PostRequestResponseModel;
import org.gamed.timelineservice.services.LikeRetrievalService;
import org.gamed.timelineservice.services.UserRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/home")
public class FrontEndController {

    @Autowired
    private UserRetrievalService userRetrievalService;
    @Autowired
    private LikeRetrievalService likeRetrievalService;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/{userID}")
    public ResponseEntity<List<PostRequestResponseModel>> getTimeline(@PathVariable String userID){


        List<String> currentUserFollowing = userRetrievalService.retrieveFollowList(userID);
        List<PostRequestResponseModel> responseList = userRetrievalService.retrieveAllPosts(currentUserFollowing);
        responseList.addAll(userRetrievalService.retrieveAllLists(currentUserFollowing));
        responseList.addAll(likeRetrievalService.getFriendsLikePost(currentUserFollowing));

        responseList.sort(Comparator.comparing(PostRequestResponseModel::getTimestamp).reversed());

        return ResponseEntity.ok(responseList);
    }


}
