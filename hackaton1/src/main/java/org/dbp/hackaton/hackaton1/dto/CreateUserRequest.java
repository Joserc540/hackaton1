package org.dbp.hackaton.hackaton1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String name;

    @Email(message = "El email del usuario debe ser válido")
    @NotBlank(message = "El email del usuario es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña del admin es obligatoria")
    private String password;
}
