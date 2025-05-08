package org.dbp.hackaton.hackaton1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.dbp.hackaton.hackaton1.config.security.JwtProperties;
import org.dbp.hackaton.hackaton1.config.security.JwtUtils;
import org.dbp.hackaton.hackaton1.domain.Role;
import org.dbp.hackaton.hackaton1.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    private JwtUtils jwtUtils;
    private JwtProperties jwtProperties;
    private final String secret = "mySecretKey";
    private final long expiration = 3600000;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret(secret);
        jwtProperties.setExpiration(expiration);
        jwtUtils = new JwtUtils(jwtProperties);
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", expiration);
    }

    @Test
    void generateTokenAndValidate() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ROLE_USER);
        String token = jwtUtils.generateToken(user);
        assertTrue(jwtUtils.validateToken(token));
        assertEquals("test@example.com", jwtUtils.extractUsername(token));
    }

    @Test
    void validateInvalidToken() {
        assertFalse(jwtUtils.validateToken("invalid.token"));
    }

    @Test
    void extractTokenFromHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer my.jwt.token");
        assertEquals("my.jwt.token", jwtUtils.extractToken(request));
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        request2.addHeader("Authorization", "Basic abc123");
        assertNull(jwtUtils.extractToken(request2));
    }

    @Test
    void generateTokenFromUsername() {
        String username = "user123";
        String token = jwtUtils.generateTokenFromUsername(username);
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        assertEquals(username, claims.getSubject());
        assertTrue(claims.getExpiration().after(new Date()));
    }
}