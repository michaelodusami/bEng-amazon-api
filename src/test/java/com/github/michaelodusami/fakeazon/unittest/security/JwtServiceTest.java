package com.github.michaelodusami.fakeazon.unittest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.michaelodusami.fakeazon.security.JwtService;

public class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testGenerateToken() {
        // Arrange
        String email = "user@example.com";

        // Act
        String token = jwtService.generateToken(email);

        // Assert
        assertNotNull(token);
        assertEquals(email, jwtService.extractUsername(token));
    }

    @Test
    void testExtractUsername() {
        // Arrange
        String email = "user@example.com";
        String token = jwtService.generateToken(email);

        // Act
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertEquals(email, extractedUsername);
    }

    @Test
    void testExtractExpiration() {
        // Arrange
        String email = "user@example.com";
        String token = jwtService.generateToken(email);

        // Act
        Date expiration = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // Ensure expiration is in the future
    }

    @Test
    public void testValidateToken_Success() {
        // Arrange
        String email = "user@example.com";
        String token = jwtService.generateToken(email);

        UserDetails userDetails = new UserDetails() {
            @Override
            public String getUsername() {
                return email;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    
}
