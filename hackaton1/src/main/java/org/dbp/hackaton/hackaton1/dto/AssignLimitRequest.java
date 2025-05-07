package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;

@Data
public class AssignLimitRequest {
    private String modelType;
    private int maxRequests;
    private int maxTokens;
    private String timeWindow;
}
