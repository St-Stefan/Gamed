package org.gamed.userdatabaseservice.integration;

import org.gamed.userdatabaseservice.domain.User;
import org.gamed.userdatabaseservice.repository.UserRepository;
import org.gamed.userdatabaseservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceIntegrationTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUserSuccess() {
        String name = "Test User";
        String email = "test@example.com";
        String pwdHash = "hashedpassword";
        boolean developer = false;
        boolean premium = false;

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return user;
        });

        User result = userService.createUser(name, email, pwdHash, developer, premium);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(pwdHash, result.getPwdHash());
        assertEquals(developer, result.isDeveloper());
        assertEquals(premium, result.isPremium());
        assertNull(result.getId());

        verify(userRepository).existsByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUserThrowsExceptionWhenEmailIsBlank() {
        String name = "Test User";
        String email = "";
        String pwdHash = "hashedpassword";
        boolean developer = false;
        boolean premium = false;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(name, email, pwdHash, developer, premium)
        );

        assertEquals("Email must be unique and cannot be blank.", exception.getMessage());

        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUserThrowsExceptionWhenEmailIsNull() {
        String name = "Test User";
        String email = null;
        String pwdHash = "hashedpassword";
        boolean developer = false;
        boolean premium = false;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(name, email, pwdHash, developer, premium)
        );

        assertEquals("Email must be unique and cannot be blank.", exception.getMessage());

        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUserThrowsExceptionWhenEmailNotUnique() {
        String name = "Test User";
        String email = "test@example.com";
        String pwdHash = "hashedpassword";
        boolean developer = false;
        boolean premium = false;

        when(userRepository.existsByEmail(email)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(name, email, pwdHash, developer, premium)
        );

        assertEquals("Email must be unique and cannot be blank.", exception.getMessage());

        verify(userRepository).existsByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserSuccess() {
        String id = "userId123";
        User user = new User("Test User", "test@example.com", "hashedpassword", false, false);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getUser(id);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository).findById(id);
    }

    @Test
    void testGetUserThrowsExceptionWhenNotFound() {
        String id = "userId123";

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.getUser(id)
        );

        assertEquals("Given User id does not correspond to any User. ID: " + id, exception.getMessage());

        verify(userRepository).findById(id);
    }

    @Test
    void testUpdateUserSuccess() {
        String id = "userId123";
        User existingUser = new User("Old Name", "old@example.com", "oldpwdhash", false, false);

        String newName = "New Name";
        String newEmail = "new@example.com";
        String newPwdHash = "newpwdhash";
        boolean newDeveloper = true;
        boolean newPremium = true;

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(id, newName, newEmail, newPwdHash, newDeveloper, newPremium);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(newEmail, result.getEmail());
        assertEquals(newPwdHash, result.getPwdHash());
        assertEquals(newDeveloper, result.isDeveloper());
        assertEquals(newPremium, result.isPremium());

        verify(userRepository).findById(id);
        verify(userRepository).existsByEmail(newEmail);
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUserThrowsExceptionWhenEmailNotUnique() {
        String id = "userId123";
        User existingUser = new User("Old Name", "old@example.com", "oldpwdhash", false, false);

        String newEmail = "existing@example.com";

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(id, null, newEmail, null, false, false)
        );

        assertEquals("Email must be unique and cannot be blank.", exception.getMessage());

        verify(userRepository).findById(id);
        verify(userRepository).existsByEmail(newEmail);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUserSuccess() {
        String id = "userId123";

        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void testDeleteUserThrowsExceptionWhenNotFound() {
        String id = "userId123";

        when(userRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.deleteUser(id)
        );

        assertEquals("Given User id does not correspond to any User. ID: " + id, exception.getMessage());

        verify(userRepository).existsById(id);
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void testUserExistsByEmail() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userService.userExistsByEmail(email);

        assertTrue(result);

        verify(userRepository).existsByEmail(email);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User("User1", "user1@example.com", "pwdhash1", false, false),
                new User("User2", "user2@example.com", "pwdhash2", true, true)
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(users, result);

        verify(userRepository).findAll();
    }
}
