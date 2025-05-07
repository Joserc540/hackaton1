package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;
import org.dbp.hackaton.hackaton1.domain.ModelType;

@Data
public class AIResponseDTO {
    private String responseText;
    private int tokensUsed;
    private ModelType modelType;
    private boolean success;
    private String errorMessage;
}
