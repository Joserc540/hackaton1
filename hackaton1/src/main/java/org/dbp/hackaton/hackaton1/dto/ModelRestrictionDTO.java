package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;
import org.dbp.hackaton.hackaton1.domain.ModelType;

@Data
public class ModelRestrictionDTO {
    private Long id;
    private ModelType modelType;
    private int maxRequests;
    private int maxTokens;
    private String timeWindow;
    private Long companyId;
}
