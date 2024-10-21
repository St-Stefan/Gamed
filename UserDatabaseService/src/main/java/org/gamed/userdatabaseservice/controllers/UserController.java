package org.gamed.userdatabaseservice.controllers;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.models.CreateAndUpdateUserRequestModel;
import org.gamed.userdatabaseservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileWriter;
import java.util.List;

/**
 * Controller for managing the User entity.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final transient UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new User and stores it in the database.
     *
     * @param request  the data the caller must provide in order to create a user
     * @return the ID of the newly created user
     */
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody CreateAndUpdateUserRequestModel request) {
        try {
            if (userService.userExistsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists.");
            }
            User newUser = userService.createUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPwdHash(),
                    request.isDeveloper(),
                    request.isPremium()
            );
            return ResponseEntity.ok(newUser.getId());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to update user information.
     *
     * @param userId the ID of the user to be updated
     * @param request  the data the caller must provide in order to update a user
     * @return HTTP status OK if the operation succeeds
     */
    @PutMapping("/update/{userId}")
    public HttpStatus updateUser(@PathVariable String userId, @RequestBody CreateAndUpdateUserRequestModel request) {
        try {
            userService.updateUser(userId, request.getUsername(), request.getEmail(), request.getPwdHash(), request.isDeveloper(), request.isPremium());
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to get user details by user ID.
     *
     * @param userId the ID of the user
     * @return the User object if found
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        try {
            User user = userService.getUser(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint to get a list of all users.
     *
     * @return a list of all User objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint to delete a user by ID.
     *
     * @param userId the ID of the user to be deleted
     * @return HTTP status OK if the operation succeeds
     */
    @DeleteMapping("/delete/{userId}")
    public HttpStatus deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
