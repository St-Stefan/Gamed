package org.gamed.gamelistdatabaseservice.service;

import org.gamed.gamelistdatabaseservice.domain.ListToGame;
import org.gamed.gamelistdatabaseservice.repository.ListToGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListToGameService {
    private final ListToGameRepository listToGameRepository;
    private final GameService gameService;
    private final GameListService gameListService;

    @Autowired
    public ListToGameService(ListToGameRepository listToGameRepository, GameService gameService, GameListService gameListService) {
        this.listToGameRepository = listToGameRepository;
        this.gameService = gameService;
        this.gameListService = gameListService;
    }

    /**
     * Creates a new ListToGame association and stores it in the database.
     *
     * @param listId id of the list
     * @param gameId id of the game
     * @return the newly created ListToGame object
     */
    public ListToGame createListToGame(String listId, String gameId) {
        ListToGame listToGame = new ListToGame(listId, gameId);
        return listToGameRepository.save(listToGame);
    }

    /**
     * Retrieves a ListToGame by its ID.
     *
     * @param id The id of the ListToGame to get
     * @return the ListToGame object if found
     * @throws IllegalArgumentException when the given id does not map to any ListToGame
     */
    public ListToGame getListToGameById(String id) throws IllegalArgumentException {
        return Optional.ofNullable(listToGameRepository.getListToGameById(id))
                .orElseThrow(() -> new IllegalArgumentException("Given ListToGame id does not correspond to any ListToGame. ID: " + id));
    }

    /**
     * Retrieves all ListToGame associations for a specific list ID.
     *
     * @param listId The ID of the list to get associations for
     * @return a list of ListToGame objects
     */
    public List<ListToGame> getListToGamesByListId(String listId) {
        return listToGameRepository.getListToGamesByListId(listId);
    }

    /**
     * Retrieves all ListToGame associations for a specific game ID.
     *
     * @param gameId The ID of the game to get associations for
     * @return a list of ListToGame objects
     */
    public List<ListToGame> getListToGamesByGameId(String gameId) {
        return listToGameRepository.getListToGamesByGameId(gameId);
    }

    /**
     * Retrieves a ListToGame association by list ID and game ID.
     *
     * @param listId The ID of the list
     * @param gameId The ID of the game
     * @return the ListToGame object if found
     * @throws IllegalArgumentException when the combination does not correspond to any ListToGame
     */
    public ListToGame getListToGameByListIdAndGameId(String listId, String gameId) throws IllegalArgumentException {
        return Optional.ofNullable(listToGameRepository.getListToGameByListIdAndGameId(listId, gameId))
                .orElseThrow(() -> new IllegalArgumentException("No ListToGame found for List ID: " + listId + " and Game ID: " + gameId));
    }

    /**
     * Deletes a ListToGame by ID.
     *
     * @param id The ID of the ListToGame to delete
     * @throws IllegalArgumentException when the given id does not map to any ListToGame
     */
    public void deleteListToGameById(String id) throws IllegalArgumentException {
        if (!listToGameRepository.existsById(id)) {
            throw new IllegalArgumentException("Given ListToGame id does not correspond to any ListToGame. ID: " + id);
        }
        listToGameRepository.deleteListToGameById(id);
    }

    /**
     * Checks if a ListToGame association exists for the given list ID and game ID.
     *
     * @param listId The ID of the list
     * @param gameId The ID of the game
     * @return true if the association exists, false otherwise
     */
    public boolean existsByListIdAndGameId(String listId, String gameId) {
        return listToGameRepository.existsByListIdAndGameId(listId, gameId);
    }

    /**
     * Retrieves all ListToGame associations.
     *
     * @return a list of all ListToGame objects
     */
    public List<ListToGame> getAllListToGames() {
        return listToGameRepository.findAll();
    }
}
