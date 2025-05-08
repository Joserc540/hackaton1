package org.dbp.hackaton.hackaton1.dto;

import lombok.Data;
import org.dbp.hackaton.hackaton1.domain.ModelType;

import java.util.List;

@Data
public class AIRequestDTO {
    private ModelType modelType;
    private String prompt;
    private List<String> fileNames;
    private String fileName;
}
