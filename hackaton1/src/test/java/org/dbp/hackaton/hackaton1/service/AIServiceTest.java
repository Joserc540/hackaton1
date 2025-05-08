package org.dbp.hackaton.hackaton1.service;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.models.ChatChoice;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.CompletionsUsage;
import com.azure.ai.inference.models.ChatResponseMessage;
import org.dbp.hackaton.hackaton1.domain.*;
import org.dbp.hackaton.hackaton1.dto.AIRequestDTO;
import org.dbp.hackaton.hackaton1.dto.AIResponseDTO;
import org.dbp.hackaton.hackaton1.repository.RequestLogRepository;
import org.dbp.hackaton.hackaton1.repository.UserLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AIServiceTest {

    @Mock private AuthService authService;
    @Mock private UserLimitRepository userLimitRepository;
    @Mock private RequestLogRepository requestLogRepository;
    @Mock private ChatCompletionsClient chatClient;

    @InjectMocks private AIService aiService;

    private User testUser;
    private UserLimit testLimit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        when(authService.getAuthenticatedUser()).thenReturn(testUser);

        testLimit = new UserLimit();
        testLimit.setMaxRequests(5);
        testLimit.setMaxTokens(100);
        testLimit.setTimeWindow(Duration.ofHours(1));
        when(userLimitRepository.findByUserIdAndModelType(eq(1L), any(ModelType.class)))
                .thenReturn(Optional.of(testLimit));
    }

    @Test
    void processRequest_HappyPath() {
        AIRequestDTO request = new AIRequestDTO();
        request.setPrompt("Hello AI");
        request.setModelType(ModelType.OPENAI);

        when(requestLogRepository.countByUserIdAndModelTypeAndTimestampAfter(
                eq(1L), eq(ModelType.OPENAI), any(LocalDateTime.class)))
                .thenReturn(0L);

        when(requestLogRepository.sumTokensUsedByUserIdAndModelTypeAndTimestampAfter(
                eq(1L), eq(ModelType.OPENAI), any(LocalDateTime.class)))
                .thenReturn(0L);

        ChatChoice choice = mock(ChatChoice.class);
        ChatResponseMessage message = mock(ChatResponseMessage.class);
        when(message.getContent()).thenReturn("AI response");
        when(choice.getMessage()).thenReturn(message);

        CompletionsUsage usage = mock(CompletionsUsage.class);
        when(usage.getTotalTokens()).thenReturn(10);

        ChatCompletions completions = mock(ChatCompletions.class);
        when(completions.getChoices()).thenReturn(Collections.singletonList(choice));
        when(completions.getUsage()).thenReturn(usage);
        when(chatClient.complete(any(ChatCompletionsOptions.class))).thenReturn(completions);

        AIResponseDTO response = aiService.processRequest(request, "CHAT");

        assertTrue(response.isSuccess());
        assertEquals("AI response", response.getResponseText());
        assertEquals(10, response.getTokensUsed());

        ArgumentCaptor<RequestLog> logCaptor = ArgumentCaptor.forClass(RequestLog.class);
        verify(requestLogRepository).save(logCaptor.capture());
        RequestLog savedLog = logCaptor.getValue();
        assertEquals("Hello AI", savedLog.getPrompt());
        assertEquals("AI response", savedLog.getResponse());
        assertEquals(10, savedLog.getTokensUsed());
        assertEquals(ModelType.OPENAI, savedLog.getModelType());
    }

    @Test
    void processRequest_ExceedsRequestLimit_Throws() {
        when(requestLogRepository.countByUserIdAndModelTypeAndTimestampAfter(
                eq(1L), eq(ModelType.OPENAI), any(LocalDateTime.class)))
                .thenReturn(5L);

        AIRequestDTO request = new AIRequestDTO();
        request.setPrompt("Test");
        request.setModelType(ModelType.OPENAI);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> aiService.processRequest(request, "CHAT"));
        assertTrue(ex.getMessage().contains("número máximo de solicitudes"));
    }

    @Test
    void processRequest_ExceedsTokenLimit_Throws() {
        when(requestLogRepository.countByUserIdAndModelTypeAndTimestampAfter(
                eq(1L), eq(ModelType.OPENAI), any(LocalDateTime.class)))
                .thenReturn(0L);
        when(requestLogRepository.sumTokensUsedByUserIdAndModelTypeAndTimestampAfter(
                eq(1L), eq(ModelType.OPENAI), any(LocalDateTime.class)))
                .thenReturn(95L);

        AIRequestDTO request = new AIRequestDTO();
        request.setPrompt("Test");
        request.setModelType(ModelType.OPENAI);

        ChatResponseMessage message = mock(ChatResponseMessage.class);
        CompletionsUsage usage = mock(CompletionsUsage.class);
        when(usage.getTotalTokens()).thenReturn(10);
        ChatCompletions completions = mock(ChatCompletions.class);
        when(completions.getUsage()).thenReturn(usage);
        when(completions.getChoices()).thenReturn(Collections.emptyList());
        when(chatClient.complete(any(ChatCompletionsOptions.class))).thenReturn(completions);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> aiService.processRequest(request, "CHAT"));
        assertTrue(ex.getMessage().contains("excede el límite de tokens"));
    }
}