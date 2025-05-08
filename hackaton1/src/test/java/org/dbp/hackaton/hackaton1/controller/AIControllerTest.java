package org.dbp.hackaton.hackaton1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbp.hackaton.hackaton1.application.AIController;
import org.dbp.hackaton.hackaton1.domain.AIService;
import org.dbp.hackaton.hackaton1.domain.ModelType;
import org.dbp.hackaton.hackaton1.domain.storage.StorageService;
import org.dbp.hackaton.hackaton1.dto.AIRequestDTO;
import org.dbp.hackaton.hackaton1.dto.AIResponseDTO;
import org.dbp.hackaton.hackaton1.dto.ModelInfoDTO;
import org.dbp.hackaton.hackaton1.dto.RequestLogDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AIController.class)
class AIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AIService aiService;

    @MockBean
    private StorageService storageService;

    @Test
    void testHandleChat() throws Exception {
        AIRequestDTO requestDto = new AIRequestDTO();
        requestDto.setModelType(ModelType.OPENAI);
        requestDto.setPrompt("Hello AI");

        AIResponseDTO responseDto = new AIResponseDTO();
        responseDto.setResponseText("Hi there!");

        when(aiService.processRequest(any(AIRequestDTO.class), eq("chat")))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.response").value("Hi there!"));
    }

    @Test
    void testHandleCompletion() throws Exception {
        AIRequestDTO requestDto = new AIRequestDTO();
        requestDto.setModelType(ModelType.META);
        requestDto.setPrompt("Complete this");

        AIResponseDTO responseDto = new AIResponseDTO();
        responseDto.setResponseText("Completed text");

        when(aiService.processRequest(any(AIRequestDTO.class), eq("completion")))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/ai/completion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Completed text"));
    }

    @Test
    void testHandleMultimodalJson() throws Exception {
        AIRequestDTO requestDto = new AIRequestDTO();
        requestDto.setModelType(ModelType.MULTIMODAL);
        requestDto.setPrompt("Process files");
        requestDto.setFileNames(List.of("file1.png", "file2.png"));

        AIResponseDTO responseDto = new AIResponseDTO();
        responseDto.setResponseText("Multimodal response");

        when(aiService.processRequest(any(AIRequestDTO.class), eq("multimodal")))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/ai/multimodal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Multimodal response"));
    }

    @Test
    void testHandleMultimodalFile() throws Exception {
        AIRequestDTO requestDto = new AIRequestDTO();
        requestDto.setModelType(ModelType.MULTIMODAL);
        requestDto.setPrompt("Analyze image");

        AIResponseDTO responseDto = new AIResponseDTO();
        responseDto.setResponseText("Image analyzed");

        when(storageService.save(any())).thenReturn("uploaded-image.png");
        when(aiService.processRequest(any(AIRequestDTO.class), eq("multimodal")))
                .thenReturn(responseDto);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "dummy image content".getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(requestDto)
        );

        mockMvc.perform(multipart("/api/ai/multimodal")
                        .file(file)
                        .file(payload)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Image analyzed"));
    }

    @Test
    void testGetAvailableModels() throws Exception {
        ModelInfoDTO modelInfo = new ModelInfoDTO();
        modelInfo.setModelType(ModelType.OPENAI);
        modelInfo.setDescription("OpenAI model");
        modelInfo.setRemainingRequests(5);
        modelInfo.setRemainingTokens(1000);

        when(aiService.getAvailableModels()).thenReturn(List.of(modelInfo));

        mockMvc.perform(get("/api/ai/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].modelType").value("OPENAI"))
                .andExpect(jsonPath("$[0].description").value("OpenAI model"))
                .andExpect(jsonPath("$[0].remainingRequests").value(5))
                .andExpect(jsonPath("$[0].remainingTokens").value(1000));
    }

    @Test
    void testGetUserHistory() throws Exception {
        RequestLogDTO logDto = new RequestLogDTO();
        logDto.setId(1L);
        logDto.setPrompt("Test prompt");
        logDto.setResponse("Test response");
        logDto.setTokensUsed(10);
        logDto.setModelType(ModelType.OPENAI);
        logDto.setTimestamp(LocalDateTime.of(2025, 5, 8, 12, 0));
        logDto.setFileName("test.png");

        when(aiService.getUserRequestHistory()).thenReturn(List.of(logDto));

        mockMvc.perform(get("/api/ai/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].prompt").value("Test prompt"))
                .andExpect(jsonPath("$[0].response").value("Test response"))
                .andExpect(jsonPath("$[0].tokensUsed").value(10))
                .andExpect(jsonPath("$[0].modelType").value("OPENAI"))
                .andExpect(jsonPath("$[0].fileName").value("test.png"));
    }
}