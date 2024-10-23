package org.gamed.userdatabaseservice.controllers;

import org.gamed.userdatabaseservice.domain.UserToPlaytime;
import org.gamed.userdatabaseservice.service.UserToPlaytimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the UserToPlaytime entity.
 */
@RestController
@RequestMapping("/user/playtime")
public class UserToPlaytimeController {

    private final transient UserToPlaytimeService playtimeService;

    @Autowired
    public UserToPlaytimeController(UserToPlaytimeService playtimeService) {
        this.playtimeService = playtimeService;
    }

    /**
     * Endpoint to record playtime for a user.
     *
     * @param userId    the ID of the user
     * @param gameId    the ID of the game
     * @param playtime   the amount of playtime recorded
     * @return HTTP status OK if the operation succeeds
     */
    @PostMapping("/record")
    public ResponseEntity<String> recordPlaytime(@RequestParam String userId, @RequestParam String gameId, @RequestParam int playtime) {
        try {
            playtimeService.createPlaytime(userId, gameId, playtime);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to update playtime for a user.
     *
     * @param userId    the ID of the user
     * @param gameId    the ID of the game
     * @param playtime   the new amount of playtime
     * @return HTTP status OK if the operation succeeds
     */
    @PutMapping("/update")
    public ResponseEntity<String> updatePlaytime(@RequestParam String userId, @RequestParam String gameId, @RequestParam int playtime) {
        try {
            playtimeService.updatePlaytime(userId, gameId, playtime);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get the total playtime for a user on a specific game.
     *
     * @param userId the ID of the user
     * @param gameId the ID of the game
     * @return the total playtime of the user on the specified game
     */
    @GetMapping("/{userId}/game/{gameId}")
    public ResponseEntity<Integer> getPlaytime(@PathVariable String userId, @PathVariable String gameId) {
        try {
            int playtime = playtimeService.getPlaytime(userId, gameId);
            return ResponseEntity.ok(playtime);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get all playtime records for a specific user.
     *
     * @param userId the ID of the user
     * @return List of UserToPlaytime records for the user
     */
    @GetMapping("/{userId}/records")
    public ResponseEntity<List<UserToPlaytime>> getPlaytimeRecords(@PathVariable String userId) {
        try {
            List<UserToPlaytime> playtimeRecords = playtimeService.getPlaytimeRecords(userId);
            return ResponseEntity.ok(playtimeRecords);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete playtime record for a user on a specific game.
     *
     * @param userId the ID of the user
     * @param gameId the ID of the game
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePlaytime(@RequestParam String userId, @RequestParam String gameId) {
        try {
            playtimeService.deletePlaytime(userId, gameId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
