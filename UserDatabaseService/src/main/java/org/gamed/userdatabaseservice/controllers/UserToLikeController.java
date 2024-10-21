package org.gamed.userdatabaseservice.controllers;

import org.gamed.userdatabaseservice.domain.UserToLike;
import org.gamed.userdatabaseservice.service.UserToLikeService;
import org.gamed.userdatabaseservice.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing the UserToLike entity.
 */
@RestController
@RequestMapping("/user/likes")
public class UserToLikeController {

    private final transient UserToLikeService likeService;

    @Autowired
    public UserToLikeController(UserToLikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * Endpoint to like an item.
     *
     * @param userId the ID of the user liking the item
     * @param itemId the ID of the item being liked
     * @param type   the type of the item
     * @return HTTP status OK if the operation succeeds
     */
    @PostMapping("/like")
    public HttpStatus likeItem(@RequestParam String userId, @RequestParam String itemId, @RequestParam String type) {
        try {
            likeService.createLike(userId, itemId, type);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to unlike an item.
     *
     * @param userId the ID of the user unliking the item
     * @param itemId the ID of the item being unliked
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/unlike")
    public HttpStatus unlikeItem(@RequestParam String userId, @RequestParam String itemId) {
        try {
            likeService.deleteLike(userId, itemId);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get all items liked by a user.
     *
     * @param userId the ID of the user
     * @return List of liked items
     */
    @GetMapping("/{userId}/liked-items")
    public ResponseEntity<List<String>> getLikedItems(@PathVariable String userId) {
        try {
            List<UserToLike> likedItems = likeService.getLikedItems(userId);
            return ResponseEntity.ok(likedItems.stream().map(UserToLike::getItemId).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get all users who liked a specific item.
     *
     * @param itemId the ID of the item
     * @return List of users who liked the item
     */
    @GetMapping("/{itemId}/likers")
    public ResponseEntity<List<User>> getLikers(@PathVariable String itemId) {
        try {
            List<User> likers = likeService.getLikers(itemId);
            return ResponseEntity.ok(likers);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to check if a user likes an item.
     *
     * @param userId the ID of the user
     * @param itemId the ID of the item
     * @return HTTP status OK if liking, BAD REQUEST otherwise
     */
    @GetMapping("/is-liking")
    public HttpStatus isLikingItem(@RequestParam String userId, @RequestParam String itemId) {
        try {
            boolean isLiking = likeService.isLikingItem(userId, itemId);
            return isLiking ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
