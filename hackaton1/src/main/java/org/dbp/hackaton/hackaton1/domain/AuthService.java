package org.dbp.hackaton.hackaton1.domain;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.config.exception.ResourceNotFoundException;
import org.dbp.hackaton.hackaton1.config.security.JwtUtils;
import org.dbp.hackaton.hackaton1.dto.JwtResponse;
import org.dbp.hackaton.hackaton1.dto.LoginRequest;
import org.dbp.hackaton.hackaton1.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public JwtResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String token = jwtUtils.generateToken(user);

        JwtResponse response = new JwtResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public JwtResponse refreshToken(String token) {
        if (token == null || !jwtUtils.validateToken(token)) {
            throw new BadCredentialsException("Token inválido o expirado");
        }

        String username = jwtUtils.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        String newToken = jwtUtils.generateTokenFromUsername(user.getUsername());
        return JwtResponse.builder()
                .token(newToken)
                .type("Bearer")
                .expiresIn(jwtUtils.getJwtExpirationMs())
                .username(user.getUsername())
                .roles(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
    }
}
