package org.dbp.hackaton.hackaton1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type;
    private long expiresIn;
    private String username;
    private List<String> roles;
    private String email;
    private String role;
}
