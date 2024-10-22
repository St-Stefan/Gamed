package org.gamed.userdatabaseservice.controllers;

import org.gamed.userdatabaseservice.service.UserToFollowedUserService;
import org.gamed.userdatabaseservice.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the UserToFollowedUser entity.
 */
@RestController
@RequestMapping("/user/follow")
public class UserToFollowedUserController {

    private final transient UserToFollowedUserService followedUserService;

    @Autowired
    public UserToFollowedUserController(UserToFollowedUserService followedUserService) {
        this.followedUserService = followedUserService;
    }

    /**
     * Endpoint to follow a user.
     *
     * @param followerId the ID of the user who follows another
     * @param followedId the ID of the user being followed
     * @return HTTP status OK if the operation succeeds
     */
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestParam String followerId, @RequestParam String followedId) {
        try {
            followedUserService.createFollowedUser(followerId, followedId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to unfollow a user.
     *
     * @param followerId the ID of the user who unfollows another
     * @param followedId the ID of the user being unfollowed
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestParam String followerId, @RequestParam String followedId) {
        try {
            followedUserService.deleteFollowedUser(followerId, followedId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get the list of users followed by a specific user.
     *
     * @param userId the ID of the user
     * @return List of followed users
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable String userId) {
        try {
            List<User> followingList = followedUserService.getFollowing(userId);
            return ResponseEntity.ok(followingList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get the list of followers for a specific user.
     *
     * @param userId the ID of the user
     * @return List of followers
     */
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable String userId) {
        try {
            List<User> followersList = followedUserService.getFollowers(userId);
            return ResponseEntity.ok(followersList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to check if a user is following another user.
     *
     * @param followerId the ID of the follower
     * @param followedId the ID of the user being followed
     * @return HTTP status OK if following, BAD REQUEST otherwise
     */
    @GetMapping("/is-following")
    public ResponseEntity<String> isFollowing(@RequestParam String followerId, @RequestParam String followedId) {
        try {
            boolean isFollowing = followedUserService.isFollowing(followerId, followedId);
            return isFollowing ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
