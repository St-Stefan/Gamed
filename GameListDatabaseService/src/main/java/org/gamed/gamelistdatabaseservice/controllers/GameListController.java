package org.gamed.gamelistdatabaseservice.controllers;

import org.gamed.gamelistdatabaseservice.domain.GameList;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateListRequestModel;
import org.gamed.gamelistdatabaseservice.service.GameListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the List entity.
 */
@RestController
@RequestMapping("/lists")
public class GameListController {

    private final GameListService gameListService;

    @Autowired
    public GameListController(GameListService gameListService) {
        this.gameListService = gameListService;
    }

    /**
     * Creates a new List and stores it in the database.
     *
     * @param request the data the caller must provide to create a list
     * @return the ID of the newly created list
     */
    @PostMapping("/create")
    public ResponseEntity<String> createList(@RequestBody CreateAndUpdateListRequestModel request) {
        try {
            GameList newList = gameListService.createList(request.getUserId(),request.getName(), request.getDescription());
            return ResponseEntity.ok(newList.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to update list information.
     *
     * @param listId the ID of the list to be updated
     * @param request the data the caller must provide to update a list
     * @return HTTP status OK if the operation succeeds
     */
    @PutMapping("/update/{listId}")
    public ResponseEntity<String> updateList(@PathVariable String listId, @RequestBody CreateAndUpdateListRequestModel request) {
        try {
            gameListService.updateList(listId, request.getName(), request.getUserId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get list details by list ID.
     *
     * @param listId the ID of the list
     * @return the List object if found
     */
    @GetMapping("/{listId}")
    public ResponseEntity<GameList> getList(@PathVariable String listId) {
        try {
            GameList list = gameListService.getList(listId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a list by ID.
     *
     * @param listId the ID of the list to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{listId}")
    public ResponseEntity<String> deleteList(@PathVariable String listId) {
        try {
            gameListService.deleteList(listId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get all lists created by a user.
     *
     * @param userId the ID of the user to get the lists from
     * @return HTTP status OK if the operation succeeds
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GameList>> getListByUserId(@PathVariable String userId) {
        try {
            List<GameList> listOfLists = gameListService.getListsByUserId(userId);
            return ResponseEntity.ok(listOfLists);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get all existing lists.
     *
     * @return HTTP status OK if the operation succeeds
     */
    @GetMapping("/all")
    public ResponseEntity<List<GameList>> getAllLists() {
        try {
            List<GameList> lists = gameListService.getAllLists();
            return ResponseEntity.ok(lists);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
