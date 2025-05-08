package org.dbp.hackaton.hackaton1.repository;

import org.dbp.hackaton.hackaton1.domain.ModelType;
import org.dbp.hackaton.hackaton1.domain.User;
import org.dbp.hackaton.hackaton1.domain.UserLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserLimitRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserLimitRepository repository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("secret");
        em.persist(user);
    }

    @Test
    void findByUserId_ReturnsAllLimitsForUser() {
        UserLimit limitA = new UserLimit();
        limitA.setUser(user);
        limitA.setModelType(ModelType.OPENAI);
        limitA.setMaxRequests(10);
        limitA.setMaxTokens(1000);
        limitA.setTimeWindow(Duration.ofHours(1));
        em.persist(limitA);

        UserLimit limitB = new UserLimit();
        limitB.setUser(user);
        limitB.setModelType(ModelType.META);
        limitB.setMaxRequests(5);
        limitB.setMaxTokens(500);
        limitB.setTimeWindow(Duration.ofMinutes(30));
        em.persist(limitB);

        em.flush();

        List<UserLimit> limits = repository.findByUserId(user.getId());
        assertEquals(2, limits.size(), "Debe devolver dos límites para el usuario");
    }

    @Test
    void findByUserIdAndModelType_ReturnsCorrectLimit() {
        UserLimit expected = new UserLimit();
        expected.setUser(user);
        expected.setModelType(ModelType.DEEPSPEAK);
        expected.setMaxRequests(7);
        expected.setMaxTokens(700);
        expected.setTimeWindow(Duration.ofMinutes(45));
        em.persist(expected);

        UserLimit other = new UserLimit();
        other.setUser(user);
        other.setModelType(ModelType.OPENAI);
        other.setMaxRequests(3);
        other.setMaxTokens(300);
        other.setTimeWindow(Duration.ofMinutes(15));
        em.persist(other);

        em.flush();

        Optional<UserLimit> opt = repository.findByUserIdAndModelType(
                user.getId(), ModelType.DEEPSPEAK);
        assertTrue(opt.isPresent(), "Debe encontrar un límite para el modelo especificado");
        UserLimit found = opt.get();
        assertEquals(expected.getMaxRequests(), found.getMaxRequests(), "maxRequests debe coincidir");
        assertEquals(expected.getMaxTokens(), found.getMaxTokens(), "maxTokens debe coincidir");
        assertEquals(expected.getTimeWindow(), found.getTimeWindow(), "timeWindow debe coincidir");
    }
}
