package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;
import org.dbp.hackaton.hackaton1.domain.ModelType;

@Data
public class AssignLimitRequest {
    private ModelType modelType;
    private int maxRequests;
    private int maxTokens;
    private String timeWindow;
}
