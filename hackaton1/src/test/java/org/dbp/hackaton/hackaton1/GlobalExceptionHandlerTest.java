package security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private final String jwtSecret = "testSecret";
    private final int jwtExpirationMs = 3600000;

    private final JwtUtils jwtUtils = new JwtUtils(jwtSecret, jwtExpirationMs);

    @Test
    void generateJwtToken_ShouldReturnToken() {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtUtils.generateJwtToken(userDetails);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void getUserNameFromJwtToken_ShouldReturnUsername() {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtUtils.generateJwtToken(userDetails);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("test@example.com", username);
    }
}