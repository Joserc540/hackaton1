package org.dbp.hackaton.hackaton1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.dbp.hackaton.hackaton1.domain.ModelType;

@Data
public class AssignLimitRequest {
    @NotBlank(message = "El modelo es obligatorio")
    private ModelType modelType;

    @NotBlank(message = "El límite de requests es obligatorio")
    private int maxRequests;

    @NotBlank(message = "El límite de tokens es obligatorio")
    private int maxTokens;

    @NotBlank(message = "El timeWindow es obligatorio")
    private String timeWindow;
}
