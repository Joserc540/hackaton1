package org.dbp.hackaton.hackaton1.domain;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.dto.AIRequestDTO;
import org.dbp.hackaton.hackaton1.dto.AIResponseDTO;
import org.dbp.hackaton.hackaton1.dto.ModelInfoDTO;
import org.dbp.hackaton.hackaton1.dto.RequestLogDTO;
import org.dbp.hackaton.hackaton1.repository.RequestLogRepository;
import org.dbp.hackaton.hackaton1.repository.UserLimitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {
    private final AuthService authService;
    private final UserLimitRepository userLimitRepository;
    private final RequestLogRepository requestLogRepository;

    public AIResponseDTO processRequest(AIRequestDTO request, String type) {
        User user = authService.getAuthenticatedUser();

        UserLimit limit = userLimitRepository.findByUserIdAndModelType(user.getId(), request.getModelType())
                .orElseThrow(() -> new RuntimeException("No tienes permiso para usar este modelo"));

        // Aquí debes agregar lógica real para:
        // - Verificar uso acumulado (requests y tokens)
        // - Llamar a una API externa o un modelo real
        // - Calcular tokens usados

        // TODO: Agregar integración con modelo IA real
        String generatedResponse = "";  // Resultado de IA
        int tokensUsed = 0;             // Calculado dinámicamente

        // TODO: Validar límites reales
        // if (tokensUsados > limit.getMaxTokens() || requestsHechos > limit.getMaxRequests()) { ... }

        RequestLog log = new RequestLog();
        log.setPrompt(request.getPrompt());
        log.setResponse(generatedResponse);
        log.setTokensUsed(tokensUsed);
        log.setTimestamp(LocalDateTime.now());
        log.setModelType(request.getModelType());
        log.setFileName(request.getFileNames() != null ? String.join(",", request.getFileNames()) : null);
        log.setUser(user);
        log.setCompany(user.getCompany());
        requestLogRepository.save(log);

        AIResponseDTO response = new AIResponseDTO();
        response.setResponseText(generatedResponse);
        response.setTokensUsed(tokensUsed);
        response.setModelType(request.getModelType());
        response.setSuccess(true);
        return response;
    }

    public List<RequestLogDTO> getUserRequestHistory() {
        User user = authService.getAuthenticatedUser();
        return requestLogRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ModelInfoDTO> getAvailableModels() {
        User user = authService.getAuthenticatedUser();

        return userLimitRepository.findByUserId(user.getId()).stream()
                .map(limit -> {
                    ModelInfoDTO dto = new ModelInfoDTO();
                    dto.setModelType(limit.getModelType());
                    dto.setDescription("Modelo disponible: " + limit.getModelType());
                    dto.setRemainingRequests(limit.getMaxRequests());
                    dto.setRemainingTokens(limit.getMaxTokens());
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
