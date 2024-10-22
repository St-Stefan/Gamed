package org.gamed.userdatabaseservice.service;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToLike;
import org.gamed.userdatabaseservice.repository.UserRepository;
import org.gamed.userdatabaseservice.repository.UserToLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserToLikeService {
    private final UserToLikeRepository likeRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserToLikeService(UserToLikeRepository likeRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new UserToLike and stores it in the database.
     *
     * @param userId      the id of the user who liked something
     * @param likedItemId the id of the liked item
     * @return the newly created UserToLike object
     * @throws IllegalArgumentException if the user ids are null.
     */
    public UserToLike createLike(String userId, String likedItemId, String type) throws IllegalArgumentException {
        UserToLike like = new UserToLike(userId, likedItemId, type);
        return likeRepository.save(like);
    }

    /**
     * Gets the UserToLike by id from the database.
     *
     * @param id The id of the UserToLike to get
     * @return the UserToLike mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any UserToLike
     */
    public UserToLike getLike(String id) throws IllegalArgumentException {
        Optional<UserToLike> like = likeRepository.findById(id);
        if (like.isEmpty()) {
            throw new IllegalArgumentException("Given UserToLike id does not correspond to any UserToLike. ID: " + id);
        }
        return like.get();
    }

    /**
     * Retrieves all items liked by a specific user.
     *
     * @param userId the ID of the user
     * @return List of UserToLike records associated with the user
     */
    public List<UserToLike> getLikedItems(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        return likeRepository.findByUserId(userId);
    }

    /**
     * Deletes a UserToLike by id from the database.
     *
     * @param id The id of the UserToLike to delete
     * @throws IllegalArgumentException when the given id does not map to any UserToLike
     */
    public void deleteLike(String id) throws IllegalArgumentException {
        if (!likeRepository.existsById(id)) {
            throw new IllegalArgumentException("Given UserToLike id does not correspond to any UserToLike. ID: " + id);
        }
        likeRepository.deleteById(id);
    }

    /**
     * Deletes the like record for a specific user and item.
     *
     * @param userId the ID of the user
     * @param itemId the ID of the item
     * @throws IllegalArgumentException if no like record exists for the given user and item
     */
    public void deleteLike(String userId, String itemId) throws IllegalArgumentException {
        UserToLike likeEntry = likeRepository.findByUserAndItemId(userId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("No like record found for the given user and item"));
        likeRepository.delete(likeEntry);
    }

    /**
     * Checks if a user likes a specific item.
     *
     * @param userId the ID of the user
     * @param itemId the ID of the item
     * @return true if the user likes the item, false otherwise
     */
    public boolean isLikingItem(String userId, String itemId) {
        if (userId == null || itemId == null) {
            throw new IllegalArgumentException("User ID and Item ID cannot be null.");
        }
        return likeRepository.findByUserAndItemId(userId, itemId).isPresent();
    }


    /**
     * Retrieves all users who liked a specific item.
     *
     * @param itemId the ID of the item
     * @return List of users who liked the item
     */
    public List<String> getLikers(String itemId) {
        List<UserToLike> likes = likeRepository.findByItemId(itemId);

        return likes.stream()
                .map(UserToLike::getUser)
                .collect(Collectors.toList());
    }
}
