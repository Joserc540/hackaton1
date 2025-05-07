package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.dbp.hackaton.hackaton1.domain.ModelType;

import java.time.Duration;

@Data
public class UserLimitDTO {
    private Long id;
    private Long userId;
    private ModelType modelType;
    private int maxRequests;
    private int maxTokens;
    private String timeWindow;
}
