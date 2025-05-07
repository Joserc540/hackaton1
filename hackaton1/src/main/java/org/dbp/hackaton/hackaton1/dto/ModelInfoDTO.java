package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;
import org.dbp.hackaton.hackaton1.domain.ModelType;

@Data
public class ModelInfoDTO {
    private ModelType modelType;
    private String description;
    private int remainingRequests;
    private int remainingTokens;
}
