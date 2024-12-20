package com.github.michaelodusami.fakeazon.unittest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;
import com.github.michaelodusami.fakeazon.security.CustomUserDetailsService;
import com.github.michaelodusami.fakeazon.security.UserDetails;

public class CustomUserDeatilsServiceTest {
    
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        // Arrange
        String email = "user@example.com";
        User mockUser = User.builder()
                .email(email)
                .password("encodedPassword")
                .roles(Set.of("ROLE_USER"))
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found: " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
