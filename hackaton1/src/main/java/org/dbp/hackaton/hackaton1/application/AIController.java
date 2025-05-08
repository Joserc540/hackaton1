package org.dbp.hackaton.hackaton1.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.domain.AIService;
import org.dbp.hackaton.hackaton1.domain.storage.StorageService;
import org.dbp.hackaton.hackaton1.dto.AIRequestDTO;
import org.dbp.hackaton.hackaton1.dto.AIResponseDTO;
import org.dbp.hackaton.hackaton1.dto.ModelInfoDTO;
import org.dbp.hackaton.hackaton1.dto.RequestLogDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;
    private final StorageService storageService;

    @PostMapping("/chat")
    public ResponseEntity<AIResponseDTO> handleChat(@Valid @RequestBody AIRequestDTO request) {
        return ResponseEntity.ok(aiService.processRequest(request, "chat"));
    }

    @PostMapping("/completion")
    public ResponseEntity<AIResponseDTO> handleCompletion(@Valid @RequestBody AIRequestDTO request) {
        return ResponseEntity.ok(aiService.processRequest(request, "completion"));
    }

    @PostMapping("/multimodal")
    public ResponseEntity<AIResponseDTO> handleMultimodal(@Valid @RequestBody AIRequestDTO request) {
        return ResponseEntity.ok(aiService.processRequest(request, "multimodal"));
    }
    @PostMapping(value = "/multimodal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AIResponseDTO> handleMultimodal(@RequestPart("file") MultipartFile file, @RequestPart("payload") @Valid AIRequestDTO payload)
        throws IOException {
        String fileName = storageService.save(file);
        payload.setFileName(fileName);
        return ResponseEntity.ok(aiService.processRequest(payload, "multimodal"));
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
