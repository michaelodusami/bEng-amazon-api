package com.github.michaelodusami.fakeazon.modules.user;

import org.springframework.boot.test.context.SpringBootTest;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByEmail_Success()
    {
        // Arrange
        String email = "test@example.com";
        User mockUser = new User(1L, "Test User", email,  "passwprd", null, null, null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

         // Act
        Optional<User> user = userRepository.findByEmail(email);

        // Assert
        assertTrue(user.isPresent());
        assertEquals(email, user.get().getEmail());
        verify(userRepository, times(1)).findByEmail(email);

    }

    @Test
    void testFindByEmail_NotFound() {
        // Arrange
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> user = userRepository.findByEmail(email);

        // Assert
        assertTrue(user.isEmpty());
        verify(userRepository, times(1)).findByEmail(email);
    }
}