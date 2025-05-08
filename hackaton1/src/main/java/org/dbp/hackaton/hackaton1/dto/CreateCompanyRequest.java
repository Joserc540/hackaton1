package org.dbp.hackaton.hackaton1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCompanyRequest {
    @NotBlank(message = "El nombre de la compañía es obligatorio")
    private String name;

    @NotBlank(message = "El RUC es obligatorio")
    private String ruc;

    @NotBlank(message = "El nombre del admin es obligatorio")
    private String adminName;

    @Email(message = "El email del admin debe ser válido")
    @NotBlank(message = "El email del admin es obligatorio")
    private String adminEmail;

    @NotBlank(message = "La contraseña del admin es obligatoria")
    private String adminPassword;
}
