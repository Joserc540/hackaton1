package org.dbp.hackaton.hackaton1.domain;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.models.*;
import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.dto.AIRequestDTO;
import org.dbp.hackaton.hackaton1.dto.AIResponseDTO;
import org.dbp.hackaton.hackaton1.dto.ModelInfoDTO;
import org.dbp.hackaton.hackaton1.dto.RequestLogDTO;
import org.dbp.hackaton.hackaton1.repository.RequestLogRepository;
import org.dbp.hackaton.hackaton1.repository.UserLimitRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {
    private final AuthService authService;
    private final UserLimitRepository userLimitRepository;
    private final RequestLogRepository requestLogRepository;
    private final ChatCompletionsClient chatClient;

    @Value("${azure.ai.default-model:OPENAI}")
    private ModelType defaultModelType;

    private String getAzureModelId(ModelType modelType) {
        switch (modelType) {
            case OPENAI:
                return "openai/gpt-4.1-nano";
            case META:
                return "meta/your-meta-model";
            case DEEPSPEAK:
                return "deepspeak/your-deepspeak-model";
            case MULTIMODAL:
                return "multimodal/your-multimodal-model";
            default:
                throw new IllegalArgumentException("Unsupported model type: " + modelType);
        }
    }

    public AIResponseDTO processRequest(AIRequestDTO request, String type) {
        User user = authService.getAuthenticatedUser();
        ModelType usedModel = request.getModelType() != null ? request.getModelType() : defaultModelType;

        UserLimit limit = userLimitRepository
                .findByUserIdAndModelType(user.getId(), usedModel)
                .orElseThrow(() -> new RuntimeException("No tienes permiso para usar este modelo"));

        long requestsUsed = requestLogRepository.countByUserIdAndModelType(user.getId(), usedModel);
        if (requestsUsed >= limit.getMaxRequests()) {
            throw new RuntimeException("Has alcanzado el número máximo de solicitudes");
        }

        List<ChatRequestMessage> messages = Arrays.asList(
                new ChatRequestSystemMessage("You are a helpful assistant."),
                new ChatRequestUserMessage(request.getPrompt())
        );

        String azureModelId = getAzureModelId(usedModel);
        ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
                .setModel(azureModelId);

        ChatCompletions completions = chatClient.complete(options);
        String generatedResponse = completions.getChoices().get(0).getMessage().getContent();
        int tokensUsed = completions.getUsage().getTotalTokens();

        if (tokensUsed > limit.getMaxTokens()) {
            throw new RuntimeException("La respuesta excede el límite de tokens permitidos");
        }

        RequestLog log = new RequestLog();
        log.setPrompt(request.getPrompt());
        log.setResponse(generatedResponse);
        log.setTokensUsed(tokensUsed);
        log.setTimestamp(LocalDateTime.now());
        log.setModelType(usedModel);
        log.setFileName(request.getFileNames() != null
                ? String.join(",", request.getFileNames())
                : null);
        log.setUser(user);
        log.setCompany(user.getCompany());
        requestLogRepository.save(log);

        AIResponseDTO response = new AIResponseDTO();
        response.setResponseText(generatedResponse);
        response.setTokensUsed(tokensUsed);
        response.setModelType(usedModel);
        response.setSuccess(true);
        return response;
    }

    public List<RequestLogDTO> getUserRequestHistory() {
        User user = authService.getAuthenticatedUser();
        return requestLogRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ModelInfoDTO> getAvailableModels() {
        User user = authService.getAuthenticatedUser();
        LocalDateTime since = LocalDate.now().atStartOfDay();

        return Arrays.stream(ModelType.values())
                .map(modelType -> {
                    ModelInfoDTO dto = new ModelInfoDTO();
                    dto.setModelType(modelType);
                    dto.setDescription(getAzureModelId(modelType));

                    UserLimit limit = userLimitRepository
                            .findByUserIdAndModelType(user.getId(), modelType)
                            .orElse(null);

                    int usedRequests = limit != null
                            ? (int) requestLogRepository.countByUserIdAndModelTypeAndTimestampAfter(
                            user.getId(), modelType, since)
                            : 0;
                    int remainingRequests = limit != null
                            ? limit.getMaxRequests() - usedRequests
                            : 0;

                    long usedTokens = limit != null
                            ? requestLogRepository.sumTokensUsedByUserIdAndModelTypeAndTimestampAfter(
                            user.getId(), modelType, since)
                            : 0L;
                    int remainingTokens = limit != null
                            ? limit.getMaxTokens() - (int) usedTokens
                            : 0;

                    dto.setRemainingRequests(remainingRequests);
                    dto.setRemainingTokens(remainingTokens);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private RequestLogDTO mapToDTO(RequestLog log) {
        RequestLogDTO dto = new RequestLogDTO();
        dto.setId(log.getId());
        dto.setPrompt(log.getPrompt());
        dto.setResponse(log.getResponse());
        dto.setTokensUsed(log.getTokensUsed());
        dto.setTimestamp(log.getTimestamp());
        dto.setModelType(log.getModelType());
        dto.setFileName(log.getFileName());
        return dto;
    }
}
