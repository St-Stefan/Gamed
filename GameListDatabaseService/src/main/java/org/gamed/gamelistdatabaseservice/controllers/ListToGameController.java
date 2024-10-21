package org.gamed.gamelistdatabaseservice.controllers;

import org.gamed.gamelistdatabaseservice.domain.ListToGame;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateListToGameRequestModel;
import org.gamed.gamelistdatabaseservice.service.ListToGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing ListToGame entities.
 */
@RestController
@RequestMapping("/listToGames")
public class ListToGameController {

    private final ListToGameService listToGameService;

    @Autowired
    public ListToGameController(ListToGameService listToGameService) {
        this.listToGameService = listToGameService;
    }

    /**
     * Creates a new ListToGame and stores it in the database.
     *
     * @param request  the data the caller must provide to create a ListToGame
     * @return the ID of the newly created ListToGame
     */
    @PostMapping("/create")
    public ResponseEntity<String> createListToGame(@RequestBody CreateAndUpdateListToGameRequestModel request) {
        try {
            ListToGame newListToGame = listToGameService.createListToGame(
                    request.getListId(),
                    request.getGameId()
            );
            return ResponseEntity.ok(newListToGame.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get ListToGame details by ListToGame ID.
     *
     * @param listToGameId the ID of the ListToGame
     * @return the ListToGame object if found
     */
    @GetMapping("/{listToGameId}")
    public ResponseEntity<ListToGame> getListToGame(@PathVariable String listToGameId) {
        try {
            ListToGame listToGame = listToGameService.getListToGameById(listToGameId);
            return ResponseEntity.ok(listToGame);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get a list of all ListToGames.
     *
     * @return a list of all ListToGame objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<ListToGame>> getAllListToGames() {
        try {
            List<ListToGame> listToGames = listToGameService.getAllListToGames();
            return ResponseEntity.ok(listToGames);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a ListToGame by ID.
     *
     * @param listToGameId the ID of the ListToGame to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{listToGameId}")
    public HttpStatus deleteListToGame(@PathVariable String listToGameId) {
        try {
            listToGameService.deleteListToGameById(listToGameId);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
