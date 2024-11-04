package org.gamed.gamelistdatabaseservice.controllers;

import org.gamed.gamelistdatabaseservice.domain.GameToTag;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateGameToTagRequestModel;
import org.gamed.gamelistdatabaseservice.service.GameToTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing GameToTag entities.
 */
@RestController
@RequestMapping("/gameToTags")
@CrossOrigin(origins = "http://localhost:5173")
public class GameToTagController {

    private final GameToTagService gameToTagService;

    @Autowired
    public GameToTagController(GameToTagService gameToTagService) {
        this.gameToTagService = gameToTagService;
    }

    /**
     * Creates a new GameToTag and stores it in the database.
     *
     * @param request  the data the caller must provide to create a GameToTag
     * @return the ID of the newly created GameToTag
     */
    @PostMapping("/create")
    public ResponseEntity<String> createGameToTag(@RequestBody CreateAndUpdateGameToTagRequestModel request) {
        try {
            GameToTag newGameToTag = gameToTagService.createGameToTag(
                    request.getGameId(),
                    request.getTagId()
            );
            return ResponseEntity.ok(newGameToTag.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get GameToTag details by GameToTag ID.
     *
     * @param gameToTagId the ID of the GameToTag
     * @return the GameToTag object if found
     */
    @GetMapping("/{gameToTagId}")
    public ResponseEntity<GameToTag> getGameToTag(@PathVariable String gameToTagId) {
        try {
            GameToTag gameToTag = gameToTagService.getGameToTagById(gameToTagId);
            return ResponseEntity.ok(gameToTag);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get a list of all GameToTags.
     *
     * @return a list of all GameToTag objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<GameToTag>> getAllGameToTags() {
        try {
            List<GameToTag> gameToTags = gameToTagService.getAllGameToTags();
            return ResponseEntity.ok(gameToTags);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a GameToTag by ID.
     *
     * @param gameToTagId the ID of the GameToTag to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{gameToTagId}")
    public ResponseEntity<String> deleteGameToTag(@PathVariable String gameToTagId) {
        try {
            gameToTagService.deleteGameToTagById(gameToTagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
