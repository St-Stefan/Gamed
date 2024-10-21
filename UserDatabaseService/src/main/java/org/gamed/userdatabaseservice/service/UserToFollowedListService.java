package org.gamed.userdatabaseservice.service;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.domain.UserToFollowedList;
import org.gamed.userdatabaseservice.repository.UserRepository;
import org.gamed.userdatabaseservice.repository.UserToFollowedListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserToFollowedListService {
    private final UserToFollowedListRepository userToFollowedListRepository;
    private final UserRepository userRepository;


    @Autowired
    public UserToFollowedListService(UserToFollowedListRepository followedListRepository, UserRepository userRepository) {
        this.userToFollowedListRepository = followedListRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new UserToFollowedList and stores it in the database.
     *
     * @param user       the user who is following the list
     * @param followedListId the id of the followed list
     * @return the newly created UserToFollowedList object
     * @throws IllegalArgumentException if the user ids are null.
     */
    public UserToFollowedList createFollowedList(User user, String followedListId)
            throws IllegalArgumentException {
        if (user == null || followedListId == null) {
            throw new IllegalArgumentException("User ids cannot be null.");
        }

        UserToFollowedList followedList = new UserToFollowedList(user, followedListId);
        return userToFollowedListRepository.save(followedList);
    }

    /**
     * Gets the UserToFollowedList by id from the database.
     *
     * @param id The id of the UserToFollowedList to get
     * @return the UserToFollowedList mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any UserToFollowedList
     */
    public UserToFollowedList getFollowedList(String id) throws IllegalArgumentException {
        Optional<UserToFollowedList> followedList = userToFollowedListRepository.findById(id);
        if (followedList.isEmpty()) {
            throw new IllegalArgumentException("Given UserToFollowedList id does not correspond to any UserToFollowedList. ID: " + id);
        }
        return followedList.get();
    }

    /**
     * Deletes a UserToFollowedList by id from the database.
     *
     * @param id The id of the UserToFollowedList to delete
     * @throws IllegalArgumentException when the given id does not map to any UserToFollowedList
     */
    public void deleteFollowedList(String id) throws IllegalArgumentException {
        if (!userToFollowedListRepository.existsById(id)) {
            throw new IllegalArgumentException("Given UserToFollowedList id does not correspond to any UserToFollowedList. ID: " + id);
        }
        userToFollowedListRepository.deleteById(id);
    }

    /**
     * Retrieves a UserToFollowedList object by user ID and list ID.
     *
     * @param userId the ID of the user
     * @param listId the ID of the followed list
     * @return the UserToFollowedList object if found
     * @throws IllegalArgumentException if no UserToFollowedList is found for the given user ID and list ID
     */
    public UserToFollowedList getUserToFollowedList(String userId, String listId) throws IllegalArgumentException {
        Optional<UserToFollowedList> userToFollowedList = userToFollowedListRepository.findByUserAndListId(userRepository.getUserById(userId), listId);

        if (userToFollowedList.isPresent()) {
            return userToFollowedList.get();
        } else {
            throw new IllegalArgumentException("No UserToFollowedList found for user ID: " + userId + " and list ID: " + listId);
        }
    }

    /**
     * Retrieves the ID of the UserToFollowedList entry by userId and listId.
     *
     * @param userId the id of the user
     * @param listId the id of the list being followed
     * @return the ID of the UserToFollowedList entry
     * @throws IllegalArgumentException if the entry does not exist
     */
    public String getFollowedListId(String userId, String listId) throws IllegalArgumentException {
        Optional<UserToFollowedList> followedList = userToFollowedListRepository.findByUserAndListId(userRepository.getUserById(userId), listId);
        if (followedList.isEmpty()) {
            throw new IllegalArgumentException("Followed list entry for the given user and list does not exist.");
        }
        return followedList.get().getId();
    }

    /**
     * Retrieves a list of users who are following the given list by listId.
     *
     * @param listId the id of the list
     * @return list of users following the list
     */
    public List<User> getFollowers(String listId) {
        List<UserToFollowedList> followedListEntries = userToFollowedListRepository.findByListId(listId);
        return followedListEntries.stream()
                .map(UserToFollowedList::getUser) // extract the User from each entry
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the IDs of the lists that a user is following.
     *
     * @param userId the ID of the user
     * @return a list of followed list IDs
     */
    public List<String> getFollowedLists(String userId) {
        List<UserToFollowedList> followedLists = userToFollowedListRepository.findByUserId(userId);

        // Extract the list IDs from the UserToFollowedList entities
        return followedLists.stream()
                .map(UserToFollowedList::getListId)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a user is following a list by userId and listId.
     *
     * @param userId the id of the user
     * @param listId the id of the list being followed
     * @return true if the user is following the list, false otherwise
     */
    public boolean isFollowingList(String userId, String listId) {
        return userToFollowedListRepository.findByUserAndListId(userRepository.getUserById(userId), listId).isPresent();
    }
}

