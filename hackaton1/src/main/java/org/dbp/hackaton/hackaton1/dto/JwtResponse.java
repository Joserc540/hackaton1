package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String email;
    private String role;
}
