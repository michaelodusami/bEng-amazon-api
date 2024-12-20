package com.github.michaelodusami.fakeazon.modules.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.michaelodusami.fakeazon.common.dto.LoginRequest;
import com.github.michaelodusami.fakeazon.common.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.common.security.JwtService;
import com.github.michaelodusami.fakeazon.config.SecurityConfig;
import com.github.michaelodusami.fakeazon.modules.user.controller.UserAuthController;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.service.UserService;

@WebMvcTest(UserAuthController.class)
// /
public class UserAuthControllerTest {
    
    

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        String email = "user@example.com";
        String password = "password123";
        String token = "jwt.token.here";

        LoginRequest loginRequest = new LoginRequest(email, password);
        User mockUser = User.builder()
                .id(1L)
                .email(email)
                .name("Test User")
                .password(password)
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(email)).thenReturn(token);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"user@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", token))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("Test User"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(email);
    }

    @Test
    void testLogin_Failure() throws Exception {
        // Arrange
        String email = "user@example.com";
        String password = "wrongpassword";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"user@example.com\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtService);
    }

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("Test User", "user@example.com", "password123");
        User mockUser = User.builder()
                .id(1L)
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .password(registerRequest.getPassword())
                .build();

        when(userService.save(any(RegisterRequest.class))).thenReturn(Optional.of(mockUser));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType("application/json")
                        .content("{\"name\":\"Test User\", \"email\":\"user@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(registerRequest.getEmail()))
                .andExpect(jsonPath("$.name").value(registerRequest.getName()));

        verify(userService, times(1)).save(any(RegisterRequest.class));
    }

    @Test
    void testRegister_Failure_EmailExists() throws Exception {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("Test User", "user@example.com", "password123");

        when(userService.save(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("Email already registered"));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType("application/json")
                        .content("{\"name\":\"Test User\", \"email\":\"user@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).save(any(RegisterRequest.class));
    }
}
