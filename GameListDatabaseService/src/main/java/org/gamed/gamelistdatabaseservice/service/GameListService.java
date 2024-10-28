package org.gamed.gamelistdatabaseservice.service;

import org.gamed.gamelistdatabaseservice.domain.GameList;
import org.gamed.gamelistdatabaseservice.repository.GameListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameListService {
    private final GameListRepository gameListRepository;

    @Autowired
    public GameListService(GameListRepository listRepository) {
        this.gameListRepository = listRepository;
    }

    /**
     * Creates a new List and stores it in the database.
     *
     * @param userId      the ID of the user creating the list
     * @param name        the name of the list
     * @param description the description of the li st
     * @return the newly created List object
     * @throws IllegalArgumentException if any of the parameters are blank
     */
    public GameList createList(String userId, String name, String description)
            throws IllegalArgumentException {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be blank.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("List name cannot be blank.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be blank.");
        }

        if (gameListRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("A list with this name already exists for this user.");
        }

        GameList list = new GameList(userId, name, description);
        return gameListRepository.save(list);
    }

    /**
     * Gets the List by id from the database.
     *
     * @param id The id of the List to get
     * @return the List mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any List
     */
    public GameList getList(String id) throws IllegalArgumentException {
        Optional<GameList> list = gameListRepository.findById(id);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Given List id does not correspond to any List. ID: " + id);
        }
        return list.get();
    }

    /**
     * Updates the list's name and description.
     *
     * @param id          The id of the list to update
     * @param name        The new name for the list
     * @param description The new description for the list
     * @return the updated List object
     * @throws IllegalArgumentException if the list is not found or if name or description are blank
     */
    public GameList updateList(String id, String name, String description) throws IllegalArgumentException {
        GameList list = getList(id);

        if (name != null && name.isEmpty()) {
            throw new IllegalArgumentException("List name cannot be blank.");
        }
        if (description != null && description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be blank.");
        }

        if (name != null) {
            list.setName(name);
        }
        if (description != null) {
            list.setDescription(description);
        }
        return gameListRepository.save(list);
    }

    /**
     * Deletes a List by id from the database.
     *
     * @param id The id of the List to delete
     * @throws IllegalArgumentException when the given id does not map to any List
     */
    public void deleteList(String id) throws IllegalArgumentException {
        if (!gameListRepository.existsById(id)) {
            throw new IllegalArgumentException("Given List id does not correspond to any List. ID: " + id);
        }
        gameListRepository.deleteById(id);
    }

    /**
     * Retrieves all lists for a given user.
     *
     * @param userId the ID of the user
     * @return a list of Lists created by the given user
     */
    public List<GameList> getListsByUserId(String userId) {
        return gameListRepository.getListsByUserId(userId);
    }

    /**
     * Retrieves a list by its user ID and name.
     *
     * @param userId the ID of the user
     * @param name   the name of the list
     * @return the List with the given user ID and name
     * @throws IllegalArgumentException if no list with the given user ID and name exists
     */
    public GameList getListByUserIdAndName(String userId, String name) throws IllegalArgumentException {
        GameList list = gameListRepository.getListByUserIdAndName(userId, name);
        if (list == null) {
            throw new IllegalArgumentException("No list found with the user ID: " + userId + " and name: " + name);
        }
        return list;
    }
    /**
     * Retrieves all existing lists.
     * @return all existing lists
     */
    public List<GameList> getAllLists() {
        return gameListRepository.findAll();
    }
}