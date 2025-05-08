package org.dbp.hackaton.hackaton1.domain;

import lombok.RequiredArgsConstructor;
import org.dbp.hackaton.hackaton1.repository.RequestLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestLogService {
    private final RequestLogRepository requestLogRepository;

    public void logRequest(ModelType modelType, String prompt, String response, int tokensUsed, String fileName, User user) {
        RequestLog log = new RequestLog();
        log.setModelType(modelType);
        log.setPrompt(prompt);
        log.setResponse(response);
        log.setTokensUsed(tokensUsed);
        log.setTimestamp(LocalDateTime.now());
        log.setFileName(fileName);
        log.setUser(user);
        log.setCompany(user.getCompany());

        requestLogRepository.save(log);
    }
}
