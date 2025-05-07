package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;

@Data
public class UpdateRestrictionRequest {
    private int maxRequests;
    private int maxTokens;
    private String timeWindow;
}
