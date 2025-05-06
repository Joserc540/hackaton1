package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;
import org.dbp.hackaton.hackaton1.domain.ModelType;

@Getter
@Setter
public class RestrictionDTO {
    private Long id;
    private ModelType model;
    private int maxRequests;
    private int maxTokens;
}
