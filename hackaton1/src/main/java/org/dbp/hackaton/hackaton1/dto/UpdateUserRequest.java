package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String name;
    private String password;
    private String email;
}
