package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;
import org.dbp.hackaton.hackaton1.domain.ModelType;

import java.time.Instant;

@Getter
@Setter
public class RequestLogDTO {
    private Long id;
    private String prompt;
    private String response;
    private int tokensUsed;
    private ModelType model;
    private Instant timestamp;
    private String fileName;
}
