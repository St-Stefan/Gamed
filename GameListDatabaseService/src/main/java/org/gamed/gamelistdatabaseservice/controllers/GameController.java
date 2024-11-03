package org.gamed.gamelistdatabaseservice.controllers;

import org.gamed.gamelistdatabaseservice.domain.Game;
import org.gamed.gamelistdatabaseservice.models.CreateAndUpdateGameRequestModel;
import org.gamed.gamelistdatabaseservice.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing the Game entity.
 */
@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Creates a new Game and stores it in the database.
     *
     * @param request the data the caller must provide to create a game
     * @return the ID of the newly created game
     */
    @PostMapping("/create")
    public ResponseEntity<String> createGame(@RequestBody CreateAndUpdateGameRequestModel request) {
        try {
            Game newGame = gameService.createGame(request.getName(), request.getDeveloper(), request.getReleaseDate(), request.getPlatforms());
            return ResponseEntity.ok(newGame.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to update game information.
     *
     * @param gameId the ID of the game to be updated
     * @param request the data the caller must provide to update a game
     * @return HTTP status OK if the operation succeeds
     */
    @PutMapping("/update/{gameId}")
    public ResponseEntity<String> updateGame(@PathVariable String gameId, @RequestBody CreateAndUpdateGameRequestModel request) {
        try {
            gameService.updateGame(gameId, request.getReleaseDate(), request.getPlatforms());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get game details by game ID.
     *
     * @param gameId the ID of the game
     * @return the Game object if found
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        try {
            Game game = gameService.getGame(gameId);
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get a list of all games.
     *
     * @return a list of all Game objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<Game>> getAllGames() {
        try {
            List<Game> games = gameService.getAllGames();
            return ResponseEntity.ok(games);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get a list of games that match a certain query string.
     *
     * @param query the query string
     * @return a list of all Game objects that match the query
     */
    @GetMapping("/search/{query}")
    public ResponseEntity<List<Game>> getGamesByQuery(@PathVariable String query) {
        try {
            List<Game> games = gameService.getGamesByQuery(query);
            return ResponseEntity.ok(games);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a game by ID.
     *
     * @param gameId the ID of the game to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{gameId}")
    public ResponseEntity<String> deleteGame(@PathVariable String gameId) {
        try {
            gameService.deleteGame(gameId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
