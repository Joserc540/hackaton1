package org.dbp.hackaton.hackaton1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbp.hackaton.hackaton1.application.AuthController;
import org.dbp.hackaton.hackaton1.config.security.JwtUtils;
import org.dbp.hackaton.hackaton1.domain.AuthService;
import org.dbp.hackaton.hackaton1.dto.JwtResponse;
import org.dbp.hackaton.hackaton1.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void testLogin() throws Exception {
        // Prepare login request DTO
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password123");

        // Prepare mocked JWT response
        JwtResponse jwtResponse = JwtResponse.builder()
                .token("test-token")
                .type("Bearer")
                .expiresIn(3600L)
                .username("user")
                .roles(List.of("ROLE_USER"))
                .email("user@example.com")
                .role("USER")
                .build();

        when(authService.authenticate(any(LoginRequest.class)))
                .thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testRefresh() throws Exception {
        String rawToken = "raw-jwt-token";

        JwtResponse refreshedResponse = JwtResponse.builder()
                .token("new-jwt-token")
                .type("Bearer")
                .expiresIn(7200L)
                .username("user")
                .roles(List.of("ROLE_USER"))
                .email("user@example.com")
                .role("USER")
                .build();

        when(jwtUtils.extractToken(any()))
                .thenReturn(rawToken);
        when(authService.refreshToken(rawToken))
                .thenReturn(refreshedResponse);

        mockMvc.perform(post("/api/auth/refresh")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer some-header-value"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("new-jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(7200))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}
