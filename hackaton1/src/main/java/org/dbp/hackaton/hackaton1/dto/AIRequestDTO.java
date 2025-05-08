package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;
import org.dbp.hackaton.hackaton1.domain.ModelType;

import java.util.List;

@Getter
@Setter
public class AIRequestDTO {
    private ModelType modelType;
    private String prompt;
    private List<String> fileNames;
    private String fileName;
}
