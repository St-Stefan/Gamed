package org.gamed.gamelistdatabaseservice.service;

import org.gamed.gamelistdatabaseservice.domain.Game;
import org.gamed.gamelistdatabaseservice.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Creates a new Game and stores it in the database.
     *
     * @param name        the name of the game
     * @param developer   the developer of the game
     * @param releaseDate the release date of the game
     * @param platforms   the platforms the game is available on
     * @return the newly created Game object
     * @throws IllegalArgumentException if any of the parameters are blank
     */
    public Game createGame(String name, String developer, LocalDateTime releaseDate, String platforms)
            throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be blank.");
        }
        if (developer == null || developer.isEmpty()) {
            throw new IllegalArgumentException("Developer cannot be blank.");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("Release date cannot be blank.");
        }
        if (platforms == null || platforms.isEmpty()) {
            throw new IllegalArgumentException("Platforms cannot be blank.");
        }
        if(gameRepository.existsByName(name)) {
            throw new IllegalArgumentException("Game already exists.");
        }

        Game game = new Game(name, developer, releaseDate, platforms);
        return gameRepository.save(game);
    }

    /**
     * Gets the Game by id from the database.
     *
     * @param id The id of the Game to get
     * @return the Game mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any Game
     */
    public Game getGame(String id) throws IllegalArgumentException {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isEmpty()) {
            throw new IllegalArgumentException("Given Game id does not correspond to any Game. ID: " + id);
        }
        return game.get();
    }

    /**
     * Updates the game's release date and platforms.
     *
     * @param id          The id of the game to update
     * @param releaseDate The new release date for the game
     * @param platforms   The new platforms for the game
     * @return the updated Game object
     * @throws IllegalArgumentException if the game is not found or if releaseDate or platforms are blank
     */
    public Game updateGame(String id, LocalDateTime releaseDate, String platforms) throws IllegalArgumentException {
        Game game = getGame(id);

        if (releaseDate != null) {
            throw new IllegalArgumentException("Release date cannot be blank.");
        }
        if (platforms != null && platforms.isEmpty()) {
            throw new IllegalArgumentException("Platforms cannot be blank.");
        }

        if (releaseDate != null) {
            game.setReleaseDate(releaseDate);
        }
        if (platforms != null) {
            game.setPlatforms(platforms);
        }
        return gameRepository.save(game);
    }

    /**
     * Deletes a Game by id from the database.
     *
     * @param id The id of the Game to delete
     * @throws IllegalArgumentException when the given id does not map to any Game
     */
    public void deleteGame(String id) throws IllegalArgumentException {
        if (!gameRepository.existsById(id)) {
            throw new IllegalArgumentException("Given Game id does not correspond to any Game. ID: " + id);
        }
        gameRepository.deleteById(id);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    /**
     * Retrieves all games by a given developer.
     *
     * @param developer the name of the developer
     * @return a list of games developed by the given developer
     */
    public List<Game> getGamesByDeveloper(String developer) {
        return gameRepository.getGamesByDeveloper(developer);
    }

    /**
     * Retrieves all games released on a specific date.
     *
     * @param releaseDate the release date to search for
     * @return a list of games released on the given date
     */
    public List<Game> getGamesByReleaseDate(String releaseDate) {
        return gameRepository.getGamesByReleaseDate(releaseDate);
    }

    /**
     * Retrieves all games available on a specific platform.
     *
     * @param platform the platform to search for
     * @return a list of games available on the given platform
     */
    public List<Game> getGamesByPlatform(String platform) {
        return gameRepository.getGamesByPlatform(platform);
    }
}