package org.gamed.userdatabaseservice.service;

import org.gamed.userdatabaseservice.domain.UserToPlaytime;
import org.gamed.userdatabaseservice.repository.UserRepository;
import org.gamed.userdatabaseservice.repository.UserToPlaytimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserToPlaytimeService {
    private final UserToPlaytimeRepository playtimeRepository;
    @Autowired
    public UserToPlaytimeService(UserToPlaytimeRepository playtimeRepository) {
        this.playtimeRepository = playtimeRepository;
    }

    /**
     * Creates a new UserToPlaytime and stores it in the database.
     *
     * @param userId     the id of the user
     * @param gameId     the id of the game
     * @param playtime   the total playtime in hours
     * @return the newly created UserToPlaytime object
     * @throws IllegalArgumentException if the userId, gameId, or playtime are null or negative, or if the relation doesn't exist.
     */
    public UserToPlaytime createPlaytime(String userId, String gameId, int playtime) throws IllegalArgumentException {
        if (userId == null || gameId == null || playtime < 0) {
            throw new IllegalArgumentException("User ID, Game ID cannot be null and playtime cannot be negative.");
        }

        if(playtimeExists(userId, gameId)) {
            throw new IllegalArgumentException("Relation already exists.");
        }

        UserToPlaytime playtimeEntry = new UserToPlaytime(userId, gameId, playtime);
        return playtimeRepository.save(playtimeEntry);
    }

    /**
     * Updates the playtime for an existing UserToPlaytime entry.
     *
     * @param userId   the id of the user
     * @param gameId   the id of the game
     * @param playtime the updated total playtime in hours
     * @return the updated UserToPlaytime object
     * @throws IllegalArgumentException if the userId, gameId, or playtime are null or negative,
     *                                  or if the entry does not exist.
     */
    public UserToPlaytime updatePlaytime(String userId, String gameId, int playtime) throws IllegalArgumentException {
        if (userId == null || gameId == null || playtime < 0) {
            throw new IllegalArgumentException("User ID, Game ID cannot be null and playtime cannot be negative.");
        }

        // Check if the playtime entry exists for the user and the game
        Optional<UserToPlaytime> existingPlaytimeEntry = playtimeRepository.findByUserIdAndGameId(userId, gameId);
        if (existingPlaytimeEntry.isEmpty()) {
            throw new IllegalArgumentException("Playtime entry for the given user and game does not exist.");
        }

        // Update the playtime value
        existingPlaytimeEntry.get().setPlaytime(playtime);
        return playtimeRepository.save(existingPlaytimeEntry.get());
    }


    /**
     * Gets the UserToPlaytime by id from the database.
     *
     * @param id The id of the UserToPlaytime to get
     * @return the UserToPlaytime mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any UserToPlaytime
     */
    public UserToPlaytime getPlaytime(String id) throws IllegalArgumentException {
        Optional<UserToPlaytime> playtime = playtimeRepository.findById(id);
        if (playtime.isEmpty()) {
            throw new IllegalArgumentException("Given UserToPlaytime id does not correspond to any UserToPlaytime. ID: " + id);
        }
        return playtime.get();
    }

    /**
     * Retrieves all playtime records for a specific user.
     *
     * @param userId the ID of the user
     * @return list of UserToPlaytime records for the user
     */
    public List<UserToPlaytime> getPlaytimeRecords(String userId) {
        return playtimeRepository.findByUserId(userId);
    }

    /**
     * Retrieves the total playtime for a specific user on a specific game.
     *
     * @param userId the ID of the user
     * @param gameId the ID of the game
     * @return the total playtime of the user on the specified game
     * @throws IllegalArgumentException if no playtime record exists for the given user and game
     */
    public int getPlaytime(String userId, String gameId) throws IllegalArgumentException {
        UserToPlaytime playtimeEntry = playtimeRepository.findByUserIdAndGameId(userId, gameId)
                .orElseThrow(() -> new IllegalArgumentException("No playtime found for the given user and game"));
        return playtimeEntry.getPlaytime();
    }

    /**
     * Deletes the playtime record for a specific user and game.
     *
     * @param userId the ID of the user
     * @param gameId the ID of the game
     * @throws IllegalArgumentException if no playtime record exists for the given user and game
     */
    public void deletePlaytime(String userId, String gameId) throws IllegalArgumentException {
        UserToPlaytime playtimeEntry = playtimeRepository.findByUserIdAndGameId(userId, gameId)
                .orElseThrow(() -> new IllegalArgumentException("No playtime record found for the given user and game"));
        playtimeRepository.delete(playtimeEntry);
    }

    /**
     * Check if a specific playtime relation exists.
     *
     * @param userId The id of the User
     * @param gameId The id of the Game
     * @throws IllegalArgumentException when the given id does not map to any UserToPlaytime
     */
    public boolean playtimeExists(String userId, String gameId) throws IllegalArgumentException {
        if (userId == null || gameId == null) {
            throw new IllegalArgumentException("User ID and Game ID cannot be null.");
        }

        return playtimeRepository.findByUserIdAndGameId(userId, gameId).isPresent();
    }
}
