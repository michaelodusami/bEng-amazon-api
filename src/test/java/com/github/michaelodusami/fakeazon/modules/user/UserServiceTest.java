package com.github.michaelodusami.fakeazon.modules.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.michaelodusami.fakeazon.common.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;
import com.github.michaelodusami.fakeazon.modules.user.service.UserService;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangePassword_Success() {
        Long userId = 1L;
        String newPassword = "newSecurePassword123";
        User mockUser = new User(userId, "John Doe", "john@example.com", "oldEncodedPassword", null, null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        boolean result = userService.changePassword(userId, newPassword);

        assertTrue(result);
        assertEquals("newEncodedPassword", mockUser.getPassword());
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testChangePassword_UserNotFound() {
        Long userId = 1L;
        String newPassword = "newSecurePassword123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = userService.changePassword(userId, newPassword);

        assertFalse(result);
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        Long userId = 1L;
        User updatedUser = new User(null, "John Updated", "john.updated@example.com", "newPassword", Set.of("ROLE_ADMIN"), null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.updateUser(userId, updatedUser);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        Long userId = 1L;
        User existingUser = new User(userId, "John Doe", "john@example.com", "oldEncodedPassword", Set.of("ROLE_USER"), null, null);
        User updatedUser = new User(null, "John Updated", "john.updated@example.com", "newPassword", Set.of("ROLE_ADMIN"), null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Optional<User> result = userService.updateUser(userId, updatedUser);

        assertTrue(result.isPresent());
        assertEquals("John Updated", result.get().getName());
        assertEquals("john.updated@example.com", result.get().getEmail());
        assertEquals("newEncodedPassword", result.get().getPassword());
        assertEquals(Set.of("ROLE_ADMIN"), result.get().getRoles());
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode(updatedUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testFindAll() {
        // Arrange
        User user1 = new User(1L, "User One", "user1@example.com", "password1", null, null, null);
        User user2 = new User(2L, "User Two", "user2@example.com", "password2", null, null, null);
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<User> users = userService.findAll();

        // Assert
        assertEquals(2, users.size());
        assertEquals("User One", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User(userId, "User One", "user1@example.com", "password1", null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("User One", result.get().getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(userId);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindByEmail_Success() {
        // Arrange
        String email = "user@example.com";
        User user = new User(1L, "User One", email, "password1", null, null, null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmail_NotFound() {
        // Arrange
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testSaveUser_Success()
    {
        RegisterRequest registerRequest = new RegisterRequest("Test User", "test@example.com", "p3");
        
        User mockUser = new User();
        mockUser.setName("Test User");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        Optional<User> savedUser = userService.save(registerRequest);

        // Assert
        assertTrue(savedUser.isPresent());
        assertEquals("test@example.com", savedUser.get().getEmail());
        System.out.println(savedUser); // verify is to verify the methods were called with those params
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(passwordEncoder, times(1)).encode("p3");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveUser_EmailAlreadyExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest(null, "test@example.com", null);


        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(registerRequest);
        });

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        boolean result = userService.deleteUser(userId);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDeleteUser_Failure() {
        // Arrange
        Long userId = 1L;
        User user = new User(userId, "User One", "user@example.com", "password1", null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.deleteUser(userId);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).findById(userId);
    }
}
