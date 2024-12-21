package com.github.michaelodusami.fakeazon.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.michaelodusami.fakeazon.modules.user.controller.UserAuthController;
import com.github.michaelodusami.fakeazon.modules.user.controller.UserController;
import com.github.michaelodusami.fakeazon.modules.user.dto.AuthResponse;
import com.github.michaelodusami.fakeazon.modules.user.dto.LoginRequest;
import com.github.michaelodusami.fakeazon.modules.user.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;

import junit.extensions.TestSetup;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {
    
    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15").withDatabaseName("testdb")
    .withUsername("testuser")
    .withPassword("testpassword");


    static {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    private User admin;
    private User user;

    private String adminToken;
    private String userToken;


    @BeforeEach
    void setUp(TestInfo testInfo) throws Exception
    {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        admin = User.builder().name("Admin").password("admimnpassword").email("admin@admin.com").roles(Set.of("ROLE_ADMIN")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        user = User.builder().name("User").password("userpass").email("user@user.com").roles(Set.of("ROLE_USER")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        // register both users
        mockMvc.perform(post("/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(RegisterRequest.toRegisterRequest(user))));
        
        mockMvc.perform(post("/v1/auth/register/admin").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(RegisterRequest.toRegisterRequest(admin))));

        // login and get tokens
        MvcResult mvcResult = mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(LoginRequest.toLoginRequest(user)))).andReturn();

        userToken = mvcResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        
        mvcResult = mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(LoginRequest.toLoginRequest(admin)))).andReturn();

        adminToken = mvcResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

    }


    @Test
    void contextLoads()
    {
        assertNotNull(user);
        assertNotNull(admin);
        assertNotNull(userToken);
        assertNotNull(adminToken);
        assertNotEquals(userToken, adminToken);
    }

    @Test
    void accessAdminAuthorizedRoleAsUser() throws Exception {

        mockMvc.perform(get("/v1/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAdminAuthorizedRoleAsAdmin() throws Exception {
        mockMvc.perform(get("/v1/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

}
