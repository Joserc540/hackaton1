package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;
import org.dbp.hackaton.hackaton1.domain.Role;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
