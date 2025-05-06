package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AIResponseDTO {
    private String response;
    private int tokensUsed;
    private Instant timestamp;
}
