package org.dbp.hackaton.hackaton1.service;

import org.dbp.hackaton.hackaton1.domain.AuthService;
import org.dbp.hackaton.hackaton1.domain.User;
import org.dbp.hackaton.hackaton1.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.dbp.hackaton.hackaton1.config.security.JwtUtils;
import org.dbp.hackaton.hackaton1.dto.JwtResponse;
import org.dbp.hackaton.hackaton1.dto.LoginRequest;
import org.dbp.hackaton.hackaton1.domain.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticate_Success_ReturnsJwtResponse() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ROLE_USER);

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(any(User.class)))
                .thenReturn("fake-jwt-token");

        JwtResponse response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("ROLE_USER", response.getRole());
    }

    @Test
    void authenticate_InvalidCredentials_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");
        request.setPassword("wrongpass");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        assertThrows(BadCredentialsException.class, () -> {
            authService.authenticate(request);
        });
    }

    @Test
    void getAuthenticatedUser_Success_ReturnsUser() {
        User expectedUser = new User();
        expectedUser.setEmail("user@example.com");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(expectedUser));

        User user = authService.getAuthenticatedUser();

        assertNotNull(user);
        assertEquals("user@example.com", user.getEmail());
    }

    @Test
    void refreshToken_ValidToken_ReturnsNewJwtResponse() {
        String oldToken = "valid.token.here";
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtUtils.validateToken(oldToken)).thenReturn(true);
        when(jwtUtils.extractUsername(oldToken)).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(jwtUtils.generateTokenFromUsername("user@example.com")).thenReturn("new.token.here");
        when(jwtUtils.getJwtExpirationMs()).thenReturn(3600000L);

        JwtResponse response = authService.refreshToken(oldToken);

        assertNotNull(response);
        assertEquals("new.token.here", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(3600000L, response.getExpiresIn());
    }
}
