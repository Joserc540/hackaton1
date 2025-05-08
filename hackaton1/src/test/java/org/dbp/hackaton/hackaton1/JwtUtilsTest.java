package security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private final JwtUtils jwtUtils = new JwtUtils("testSecret", 3600000);
    private final UserDetailsServiceImpl userDetailsService = mock(UserDetailsServiceImpl.class);
    private final JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtils, userDetailsService);

    @Test
    void doFilterInternal_ShouldSetAuthentication() throws ServletException, IOException {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(userDetails);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}