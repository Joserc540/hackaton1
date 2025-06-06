package org.dbp.hackaton.hackaton1.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.config.security.JwtUtils;
import org.dbp.hackaton.hackaton1.domain.AuthService;
import org.dbp.hackaton.hackaton1.dto.JwtResponse;
import org.dbp.hackaton.hackaton1.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(HttpServletRequest req) {
        String token = jwtUtils.extractToken(req);
        return ResponseEntity.ok(authService.refreshToken(token));
    }
}
