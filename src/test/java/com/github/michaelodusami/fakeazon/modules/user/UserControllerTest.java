package com.github.michaelodusami.fakeazon.modules.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.github.michaelodusami.fakeazon.modules.user.controller.UserController;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.enums.UserRole;
import com.github.michaelodusami.fakeazon.modules.user.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User(1L, "John Doe", "john@example.com", "password", Set.of("ROLE_USER"), null, null);
        User user2 = new User(2L, "Jane Smith", "jane@example.com", "password", Set.of("ROLE_USER"), null, null);

        when(userService.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(userService, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com", "password", Set.of("ROLE_USER"), null, null);

        when(userService.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(1L);
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User(null, "John Doe", "john@example.com", "password", null, null, null);
        User createdUser = new User(1L, "John Doe", "john@example.com", "encodedPassword", Set.of("ROLE_USER"), null, null);

        when(userService.save(any(User.class), eq(UserRole.ROLE_USER))).thenReturn(Optional.of(createdUser));

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService, times(1)).save(any(User.class), eq(UserRole.ROLE_USER));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        User updatedUser = new User(null, "John Updated", null, null, null, null, null);
        User resultUser = new User(1L, "John Updated", "john@example.com", "password", null, null, null);

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(resultUser));

        mockMvc.perform(put("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Updated\"}"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testDeleteUserById_Success() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUserById_NotFound() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(false);

        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testChangePassword_Success() throws Exception {
        when(userService.changePassword(eq(1L), anyString())).thenReturn(true);

        mockMvc.perform(patch("/v1/users/1/password")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("newPassword"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).changePassword(eq(1L), eq("newPassword"));
    }

    @Test
    void testChangePassword_NotFound() throws Exception {
        when(userService.changePassword(eq(1L), anyString())).thenReturn(false);

        mockMvc.perform(patch("/v1/users/1/password")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("newPassword"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).changePassword(eq(1L), eq("newPassword"));
    }
}
