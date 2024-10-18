package org.gamed.userdatabaseservice.service;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new User and stores it in the database.
     *
     * @param name      the name of the new user
     * @param email     the email of the new user
     * @param pwd_hash  the password hash of the new user
     * @param developer whether the user is a developer
     * @param premium   whether the user is a premium member
     * @return the newly created User object
     * @throws IllegalArgumentException if the email is blank, null, or not unique.
     */
    public User createUser(String name, String email, String pwd_hash, boolean developer, boolean premium)
            throws IllegalArgumentException {
        if (email == null || email.isEmpty() || userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email must be unique and cannot be blank.");
        }

        User user = new User(name, email, pwd_hash, developer, premium);
        return userRepository.save(user);
    }

    /**
     * Gets the User by id from the database.
     *
     * @param id The id of the User to get
     * @return the User mapped to the id
     * @throws IllegalArgumentException when the given id does not map to any User
     */
    public User getUser(String id) throws IllegalArgumentException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Given User id does not correspond to any User. ID: " + id);
        }
        return user.get();
    }

    /**
     * Updates the user's information.
     *
     * @param id        The id of the user to update
     * @param name      The new name for the user
     * @param email     The new email for the user
     * @param pwd_hash  The new password hash for the user
     * @param developer Whether the user is a developer
     * @param premium   Whether the user is a premium member
     * @return the updated User object
     * @throws IllegalArgumentException if the email is blank, null, or not unique.
     */
    public User updateUser(String id, String name, String email, String pwd_hash, boolean developer, boolean premium)
            throws IllegalArgumentException {
        User user = getUser(id);

        if (email != null && !email.isEmpty() && !user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email must be unique and cannot be blank.");
        }

        user.setName(name);
        user.setEmail(email);
        user.setPwdHash(pwd_hash);
        user.setDeveloper(developer);
        user.setPremium(premium);

        return userRepository.save(user);
    }

    /**
     * Deletes a User by id from the database.
     *
     * @param id The id of the User to delete
     * @throws IllegalArgumentException when the given id does not map to any User
     */
    public void deleteUser(String id) throws IllegalArgumentException {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Given User id does not correspond to any User. ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Checks if a user exists by email.
     *
     * @param email the email of the user
     * @return true if a user with the given email exists, false otherwise
     */
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
