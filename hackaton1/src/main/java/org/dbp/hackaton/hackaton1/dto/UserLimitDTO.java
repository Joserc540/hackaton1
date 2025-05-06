package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;
import org.dbp.hackaton.hackaton1.domain.ModelType;

import java.time.Duration;

@Getter
@Setter
public class UserLimitDTO {
    private ModelType model;
    private int requestLimit;
    private int tokenLimit;
    private Duration window;
}
