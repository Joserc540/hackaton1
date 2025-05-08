package org.dbp.hackaton.hackaton1.repository;

import org.dbp.hackaton.hackaton1.domain.ModelType;
import org.dbp.hackaton.hackaton1.domain.RequestLog;
import org.dbp.hackaton.hackaton1.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RequestLogRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private RequestLogRepository repository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        em.persist(user);
    }

    @Test
    void findByUserId_ReturnsAllLogsForUser() {
        RequestLog log1 = new RequestLog();
        log1.setUser(user);
        log1.setModelType(ModelType.OPENAI);
        log1.setTimestamp(LocalDateTime.now());
        log1.setTokensUsed(5);
        em.persist(log1);

        RequestLog log2 = new RequestLog();
        log2.setUser(user);
        log2.setModelType(ModelType.META);
        log2.setTimestamp(LocalDateTime.now());
        log2.setTokensUsed(10);
        em.persist(log2);

        em.flush();

        List<RequestLog> logs = repository.findByUserId(user.getId());
        assertEquals(2, logs.size());
        assertTrue(logs.stream().anyMatch(l -> l.getTokensUsed() == 5));
        assertTrue(logs.stream().anyMatch(l -> l.getTokensUsed() == 10));
    }

    @Test
    void countByUserIdAndModelTypeAndTimestampAfter_CorrectCount() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(1);

        RequestLog oldLog = new RequestLog();
        oldLog.setUser(user);
        oldLog.setModelType(ModelType.DEEPSPEAK);
        oldLog.setTimestamp(threshold.minusHours(1));
        oldLog.setTokensUsed(3);
        em.persist(oldLog);

        RequestLog newLog1 = new RequestLog();
        newLog1.setUser(user);
        newLog1.setModelType(ModelType.DEEPSPEAK);
        newLog1.setTimestamp(LocalDateTime.now().minusMinutes(30));
        newLog1.setTokensUsed(7);
        em.persist(newLog1);

        RequestLog newLog2 = new RequestLog();
        newLog2.setUser(user);
        newLog2.setModelType(ModelType.DEEPSPEAK);
        newLog2.setTimestamp(LocalDateTime.now().minusMinutes(10));
        newLog2.setTokensUsed(2);
        em.persist(newLog2);

        em.flush();

        long count = repository.countByUserIdAndModelTypeAndTimestampAfter(
                user.getId(), ModelType.DEEPSPEAK, threshold);
        assertEquals(2, count);
    }

    @Test
    void sumTokensUsedByUserIdAndModelTypeAndTimestampAfter_CorrectSum() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);

        RequestLog oldLog = new RequestLog();
        oldLog.setUser(user);
        oldLog.setModelType(ModelType.MULTIMODAL);
        oldLog.setTimestamp(threshold.minusHours(5));
        oldLog.setTokensUsed(4);
        em.persist(oldLog);

        RequestLog logA = new RequestLog();
        logA.setUser(user);
        logA.setModelType(ModelType.MULTIMODAL);
        logA.setTimestamp(LocalDateTime.now().minusHours(10));
        logA.setTokensUsed(6);
        em.persist(logA);

        RequestLog logB = new RequestLog();
        logB.setUser(user);
        logB.setModelType(ModelType.MULTIMODAL);
        logB.setTimestamp(LocalDateTime.now().minusHours(2));
        logB.setTokensUsed(9);
        em.persist(logB);

        em.flush();

        long sum = repository.sumTokensUsedByUserIdAndModelTypeAndTimestampAfter(
                user.getId(), ModelType.MULTIMODAL, threshold);
        assertEquals(15, sum);
    }
}
