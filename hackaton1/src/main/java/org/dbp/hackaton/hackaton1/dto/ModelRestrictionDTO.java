package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;
import org.dbp.hackaton.hackaton1.domain.ModelType;

import java.time.Duration;

@Data
public class ModelRestrictionDTO {
    private Long id;
    private ModelType modelType;
    private int maxRequests;
    private int maxTokens;
    private Duration timeWindow;
    private Long companyId;
}
