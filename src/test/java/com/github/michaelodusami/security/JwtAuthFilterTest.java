package com.github.michaelodusami.security;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.github.michaelodusami.fakeazon.common.security.JwtAuthFilter;
import com.github.michaelodusami.fakeazon.common.security.JwtService;
import com.github.michaelodusami.fakeazon.common.security.UserDetails;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilterTest {

    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthFilter = new JwtAuthFilter(jwtService, userRepository); // Inject mocks via constructor
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext(); // Clear context before each test
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";
        String email = "user@example.com";

        User mockUser = User.builder()
                .email(email)
                .password("encodedPassword")
                .roles(Set.of("ROLE_USER"))
                .build();

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(jwtService.validateToken(eq(token), any(UserDetails.class))).thenReturn(true);

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        assertNotNull(securityContext.getAuthentication());
        assertEquals(email, securityContext.getAuthentication().getName());
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        // Arrange
        String token = "invalid.jwt.token";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        when(jwtService.extractUsername(token)).thenReturn("user@example.com");
        when(jwtService.validateToken(eq(token), any(UserDetails.class))).thenReturn(false);

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_MissingAuthorizationHeader() throws ServletException, IOException {
        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
