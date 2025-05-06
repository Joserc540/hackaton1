package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRestrictionRequest {
    private int maxRequests;
    private int maxTokens;
}
