package org.dbp.hackaton.hackaton1.application;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.domain.AIService;
import org.dbp.hackaton.hackaton1.dto.AIRequestDTO;
import org.dbp.hackaton.hackaton1.dto.AIResponseDTO;
import org.dbp.hackaton.hackaton1.dto.ModelInfoDTO;
import org.dbp.hackaton.hackaton1.dto.RequestLogDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    @PostMapping("/chat")
    public ResponseEntity<AIResponseDTO> handleChat(@RequestBody AIRequestDTO request) {
        return ResponseEntity.ok(aiService.processRequest(request, "chat"));
    }

    @PostMapping("/completion")
    public ResponseEntity<AIResponseDTO> handleCompletion(@RequestBody AIRequestDTO request) {
        return ResponseEntity.ok(aiService.processRequest(request, "completion"));
    }

    @PostMapping("/multimodal")
    public ResponseEntity<AIResponseDTO> handleMultimodal(@RequestBody AIRequestDTO request) {
        return ResponseEntity.ok(aiService.processRequest(request, "multimodal"));
    }

    @GetMapping("/models")
    public ResponseEntity<List<ModelInfoDTO>> getAvailableModels() {
        return ResponseEntity.ok(aiService.getAvailableModels());
    }

    @GetMapping("/history")
    public ResponseEntity<List<RequestLogDTO>> getUserHistory() {
        return ResponseEntity.ok(aiService.getUserRequestHistory());
    }
}
