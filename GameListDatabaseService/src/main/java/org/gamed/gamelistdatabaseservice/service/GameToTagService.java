package org.gamed.gamelistdatabaseservice.service;

import org.gamed.gamelistdatabaseservice.domain.GameToTag;
import org.gamed.gamelistdatabaseservice.repository.GameToTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameToTagService {
    private final GameToTagRepository gameToTagRepository;
    private final GameService gameService;
    private final TagService tagService;

    @Autowired
    public GameToTagService(GameToTagRepository gameToTagRepository, GameService gameService, TagService tagService) {
        this.gameToTagRepository = gameToTagRepository;
        this.gameService = gameService;
        this.tagService = tagService;
    }

    /**
     * Creates a new GameToTag association and stores it in the database.
     *
     * @param gameId the id of the game
     * @param tagId  the id of the tag
     * @return the newly created GameToTag object
     * @throws IllegalArgumentException - when ids are null or the relation already exists
     */
    public GameToTag createGameToTag(String gameId, String tagId) {
        if(gameId == null || tagId == null) {
            throw new IllegalArgumentException("Ids cannot be null.");
        }
        if(existsByGameIdAndTagId(gameId,tagId)) {
            throw new IllegalArgumentException("Relation already exists.");
        }

        GameToTag gameToTag = new GameToTag(gameId, tagId);
        return gameToTagRepository.save(gameToTag);
    }

    /**
     * Retrieves a GameToTag by its ID.
     *
     * @param id The id of the GameToTag to get
     * @return the GameToTag object if found
     * @throws IllegalArgumentException when the given id does not map to any GameToTag
     */
    public GameToTag getGameToTagById(String id) throws IllegalArgumentException {
        return Optional.ofNullable(gameToTagRepository.getGameToTagById(id))
                .orElseThrow(() -> new IllegalArgumentException("Given GameToTag id does not correspond to any GameToTag. ID: " + id));
    }

    /**
     * Retrieves all GameToTag associations for a specific game ID.
     *
     * @param gameId The ID of the game to get associations for
     * @return a list of GameToTag objects
     */
    public List<GameToTag> getGameToTagsByGameId(String gameId) {
        return gameToTagRepository.getGameToTagsByGameId(gameId);
    }

    /**
     * Retrieves all GameToTag associations for a specific tag ID.
     *
     * @param tagId The ID of the tag to get associations for
     * @return a list of GameToTag objects
     */
    public List<GameToTag> getGameToTagsByTagId(String tagId) {
        return gameToTagRepository.getGameToTagsByTagId(tagId);
    }

    /**
     * Retrieves a GameToTag association by game ID and tag ID.
     *
     * @param gameId The ID of the game
     * @param tagId  The ID of the tag
     * @return the GameToTag object if found
     * @throws IllegalArgumentException when the combination does not correspond to any GameToTag
     */
    public GameToTag getGameToTagByGameIdAndTagId(String gameId, String tagId) throws IllegalArgumentException {
        return Optional.ofNullable(gameToTagRepository.getGameToTagByGameIdAndTagId(gameId, tagId))
                .orElseThrow(() -> new IllegalArgumentException("No GameToTag found for Game ID: " + gameId + " and Tag ID: " + tagId));
    }

    /**
     * Deletes a GameToTag by ID.
     *
     * @param id The ID of the GameToTag to delete
     * @throws IllegalArgumentException when the given id does not map to any GameToTag
     */
    public void deleteGameToTagById(String id) throws IllegalArgumentException {
        if (!gameToTagRepository.existsById(id)) {
            throw new IllegalArgumentException("Given GameToTag id does not correspond to any GameToTag. ID: " + id);
        }
        gameToTagRepository.deleteGameToTagById(id);
    }

    /**
     * Checks if a GameToTag association exists for the given game ID and tag ID.
     *
     * @param gameId The ID of the game
     * @param tagId  The ID of the tag
     * @return true if the association exists, false otherwise
     */
    public boolean existsByGameIdAndTagId(String gameId, String tagId) {
        return gameToTagRepository.existsByGameIdAndTagId(gameId, tagId);
    }

    /**
     * Retrieves all GameToTag associations.
     *
     * @return a list of all GameToTag objects
     */
    public List<GameToTag> getAllGameToTags() {
        return gameToTagRepository.findAll();
    }
}
