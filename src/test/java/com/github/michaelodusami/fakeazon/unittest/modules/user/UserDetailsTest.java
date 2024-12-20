package com.github.michaelodusami.fakeazon.unittest.modules.user;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.security.UserDetails;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsTest {
    
    @Test
    void testUserDetailsConstructor() {
        // Arrange
        User user = User.builder()
                .email("user@example.com")
                .password("encodedPassword")
                .roles(Set.of("ROLE_USER", "ROLE_ADMIN"))
                .build();

        // Act
        UserDetails userDetails = new UserDetails(user);

        // Assert
        assertEquals("user@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());

        Set<String> expectedRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        Set<String> actualRoles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    void testGetAuthorities_EmptyRoles() {
        // Arrange
        User user = User.builder()
                .email("user@example.com")
                .password("encodedPassword")
                .roles(Set.of()) // No roles
                .build();

        // Act
        UserDetails userDetails = new UserDetails(user);

        // Assert
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void testGetPassword() {
        // Arrange
        User user = User.builder()
                .email("user@example.com")
                .password("encodedPassword")
                .roles(Set.of("ROLE_USER"))
                .build();

        // Act
        UserDetails userDetails = new UserDetails(user);

        // Assert
        assertEquals("encodedPassword", userDetails.getPassword());
    }

    @Test
    void testGetUsername() {
        // Arrange
        User user = User.builder()
                .email("user@example.com")
                .password("encodedPassword")
                .roles(Set.of("ROLE_USER"))
                .build();

        // Act
        UserDetails userDetails = new UserDetails(user);

        // Assert
        assertEquals("user@example.com", userDetails.getUsername());
    }

}
