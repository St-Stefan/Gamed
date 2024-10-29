package org.gamed.userdatabaseservice.controllers;

import org.gamed.userdatabaseservice.service.UserService;
import org.gamed.userdatabaseservice.service.UserToFollowedListService;
import org.gamed.userdatabaseservice.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the UserToFollowedList entity.
 */
@RestController
@RequestMapping("/user/followed-lists")
@CrossOrigin(origins = "http://localhost:5173")
public class UserToFollowedListController {

    private final transient UserToFollowedListService followedListService;

    @Autowired
    public UserToFollowedListController(UserToFollowedListService followedListService) {
        this.followedListService = followedListService;
    }

    /**
     * Endpoint to follow a list.
     *
     * @param userId the ID of the user following a list
     * @param listId the ID of the list being followed
     * @return HTTP status OK if the operation succeeds
     */
    @PostMapping("/follow")
    public ResponseEntity<String> followList(@RequestParam String userId, @RequestParam String listId) {
        try {
            followedListService.createFollowedList(userId, listId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to unfollow a list.
     *
     * @param userId the ID of the user unfollowing a list
     * @param listId the ID of the list being unfollowed
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowList(@RequestParam String userId, @RequestParam String listId) {
        try {
            followedListService.deleteFollowedList(followedListService.getUserToFollowedList(userId, listId).getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get the lists a user is following.
     *
     * @param userId the ID of the user
     * @return List of followed lists
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<String>> getFollowingLists(@PathVariable String userId) {
        try {
            List<String> followedLists = followedListService.getFollowedLists(userId);
            return ResponseEntity.ok(followedLists);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get the users who follow a specific list.
     *
     * @param listId the ID of the list
     * @return List of users following the list
     */
    @GetMapping("/{listId}/followers")
    public ResponseEntity<List<String>> getFollowers(@PathVariable String listId) {
        try {
            List<String> followers = followedListService.getFollowers(listId);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to check if a user is following a list.
     *
     * @param userId the ID of the user
     * @param listId the ID of the list
     * @return HTTP status OK if following, BAD REQUEST otherwise
     */
    @GetMapping("/is-following")
    public ResponseEntity<String> isFollowingList(@RequestParam String userId, @RequestParam String listId) {
        try {
            boolean isFollowing = followedListService.isFollowingList(userId, listId);
            return isFollowing ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
