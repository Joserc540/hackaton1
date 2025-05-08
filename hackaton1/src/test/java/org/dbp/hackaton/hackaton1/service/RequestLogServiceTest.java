package org.dbp.hackaton.hackaton1.service;

import org.dbp.hackaton.hackaton1.domain.Company;
import org.dbp.hackaton.hackaton1.domain.ModelType;
import org.dbp.hackaton.hackaton1.domain.RequestLog;
import org.dbp.hackaton.hackaton1.domain.RequestLogService;
import org.dbp.hackaton.hackaton1.domain.User;
import org.dbp.hackaton.hackaton1.repository.RequestLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestLogServiceTest {

    @Mock
    private RequestLogRepository requestLogRepository;

    @InjectMocks
    private RequestLogService requestLogService;

    @Captor
    private ArgumentCaptor<RequestLog> requestLogCaptor;

    @Test
    void logRequest_ShouldSaveLogWithCorrectFields() {
        ModelType modelType = ModelType.OPENAI;
        String prompt = "Test prompt";
        String response = "Test response";
        int tokensUsed = 42;
        String fileName = "file.txt";
        Company company = new Company();
        company.setId(10L);
        User user = new User();
        user.setId(20L);
        user.setCompany(company);

        requestLogService.logRequest(modelType, prompt, response, tokensUsed, fileName, user);

        verify(requestLogRepository).save(requestLogCaptor.capture());
        RequestLog savedLog = requestLogCaptor.getValue();

        assertEquals(modelType, savedLog.getModelType());
        assertEquals(prompt, savedLog.getPrompt());
        assertEquals(response, savedLog.getResponse());
        assertEquals(tokensUsed, savedLog.getTokensUsed());
        assertEquals(fileName, savedLog.getFileName());
        assertEquals(user, savedLog.getUser());
        assertEquals(company, savedLog.getCompany());
        assertNotNull(savedLog.getTimestamp());
        assertTrue(savedLog.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
