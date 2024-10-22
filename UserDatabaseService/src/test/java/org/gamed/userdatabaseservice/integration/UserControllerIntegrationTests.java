package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.controllers.UserController;
import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.models.CreateAndUpdateUserRequestModel;
import org.gamed.userdatabaseservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerIntegrationTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() {
        // Arrange
        CreateAndUpdateUserRequestModel request = new CreateAndUpdateUserRequestModel("TestUser", "test@example.com", "hashed_password", true, false);
        User mockUser = new User("TestUser", "test@example.com", "hashed_password", true, false);

        when(userService.userExistsByEmail(request.getEmail())).thenReturn(false);
        when(userService.createUser(anyString(), anyString(), anyString(), anyBoolean(), anyBoolean())).thenReturn(mockUser);

        // Act
        ResponseEntity<String> response = userController.createUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(userService, times(1)).createUser(anyString(), anyString(), anyString(), anyBoolean(), anyBoolean());
    }

    @Test
    public void testCreateUser_EmailAlreadyExists() {
        // Arrange
        CreateAndUpdateUserRequestModel request = new CreateAndUpdateUserRequestModel("TestUser", "test@example.com", "hashed_password", true, false);

        when(userService.userExistsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.createUser(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Email already exists.", exception.getReason());
        verify(userService, times(1)).userExistsByEmail(request.getEmail());
    }

    @Test
    public void testUpdateUser_Success() {
        // Arrange
        CreateAndUpdateUserRequestModel request = new CreateAndUpdateUserRequestModel("UpdatedUser", "updated@example.com", "new_hashed_password", false, true);

        // Act
        ResponseEntity<String> response = userController.updateUser("12345", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update successful!", response.getBody());
        verify(userService, times(1)).updateUser("12345", "UpdatedUser", "updated@example.com", "new_hashed_password", false, true);
    }

    @Test
    public void testGetUser_UserNotFound() {
        // Arrange
        when(userService.getUser("nonexistent-id")).thenThrow(new IllegalArgumentException("User not found."));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.getUser("nonexistent-id");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found.", exception.getReason());
        verify(userService, times(1)).getUser("nonexistent-id");
    }

    @Test
    public void testGetAllUsers_Success() {
        // Arrange
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User("User1", "user1@example.com", "hashed_password1", false, true));
        mockUsers.add(new User("User2", "user2@example.com", "hashed_password2", true, false));

        when(userService.getAllUsers()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testDeleteUser_Success() {
        // Arrange
        doNothing().when(userService).deleteUser("12345");

        // Act
        ResponseEntity<String> response = userController.deleteUser("12345");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delete successful!", response.getBody());
        verify(userService, times(1)).deleteUser("12345");
    }

    @Test
    public void testDeleteUser_NotFound() {
        // Arrange
        doThrow(new IllegalArgumentException("User not found.")).when(userService).deleteUser("nonexistent-id");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.deleteUser("nonexistent-id");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User not found.", exception.getReason());
        verify(userService, times(1)).deleteUser("nonexistent-id");
    }
}
