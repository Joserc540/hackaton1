package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CompanyConsumptionDTO {
    private Long companyId;
    private String companyName;
    private int totalRequests;
    private int totalTokens;
    private Map<String, Integer> requestsPerModel;
}
