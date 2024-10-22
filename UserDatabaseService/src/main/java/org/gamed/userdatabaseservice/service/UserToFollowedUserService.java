package org.gamed.userdatabaseservice.service;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToFollowedUser;
import org.gamed.userdatabaseservice.repository.UserRepository;
import org.gamed.userdatabaseservice.repository.UserToFollowedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserToFollowedUserService {
    private final UserToFollowedUserRepository followedUserRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserToFollowedUserService(UserToFollowedUserRepository followedUserRepository, UserRepository userRepository) {
        this.followedUserRepository = followedUserRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new UserToFollowedUser and stores it in the database.
     *
     * @param userId        the id of the user who is following
     * @param followedUserId the id of the user being followed
     * @return the newly created UserToFollowedUser object
     * @throws IllegalArgumentException if the user ids are null, or if the relation already exists.
     */
    public UserToFollowedUser createFollowedUser(String userId, String followedUserId)
            throws IllegalArgumentException {
        if (userId == null || followedUserId == null) {
            throw new IllegalArgumentException("User ids cannot be null.");
        }

        if(isFollowing(userId, followedUserId)) {
            throw new IllegalArgumentException("Relation already exists.");
        }

        UserToFollowedUser followedUser = new UserToFollowedUser(userId, followedUserId);
        return followedUserRepository.save(followedUser);
    }

    /**
     * Gets the UserToFollowedUser by id from the database.
     *
     * @param id The id of the UserToFollowedUser to get
     * @return the UserToFollowedUser mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any UserToFollowedUser
     */
    public UserToFollowedUser getFollowedUser(String id) throws IllegalArgumentException {
        Optional<UserToFollowedUser> followedUser = followedUserRepository.findById(id);
        if (followedUser.isEmpty()) {
            throw new IllegalArgumentException("Given UserToFollowedUser id does not correspond to any UserToFollowedUser. ID: " + id);
        }
        return followedUser.get();
    }

    /**
     * Get the list of followers for a specific user.
     *
     * @param userId the ID of the user
     * @return List of followers
     */
    public List<String> getFollowers(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return followedUserRepository.findFollowers(user.getId());
    }

    /**
     * Get the list of users followed by a specific user.
     *
     * @param userId the ID of the user
     * @return List of followed users
     */
    public List<String> getFollowing(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return followedUserRepository.findFollowed(user.getId());
    }

    /**
     * Check if a user is following another user.
     *
     * @param followerId the ID of the follower
     * @param followedId the ID of the user being followed
     * @return true if the follower is following the followed user, false otherwise
     */
    public boolean isFollowing(String followerId, String followedId) {
        return followedUserRepository.findByUserIdAndFollowedUserId(followerId, followedId).isPresent();
    }

    /**
     * Deletes a UserToFollowedUser from the database.
     *
     * @param followerId the ID of the follower
     * @param followedId the ID of the user being followed
     * @throws IllegalArgumentException when the given IDs do not map to any UserToFollowedUser
     */
    public void deleteFollowedUser(String followerId, String followedId) throws IllegalArgumentException {
        Optional<UserToFollowedUser> followedUser = followedUserRepository.findByUserIdAndFollowedUserId(followerId, followedId);
        if (followedUser.isEmpty()) {
            throw new IllegalArgumentException("No UserToFollowedUser found for followerId: "
                    + followerId + " and followedId: " + followedId);
        }
        followedUserRepository.deleteById(followedUser.get().getId());
    }
}
