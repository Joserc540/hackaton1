package org.dbp.hackaton.hackaton1.repository;

import org.dbp.hackaton.hackaton1.domain.ModelType;
import org.dbp.hackaton.hackaton1.domain.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
    List<RequestLog> findByUserId(Long userId);
    long countByUserIdAndModelTypeAndTimestampAfter(
            Long userId,
            ModelType modelType,
            LocalDateTime after
    );

    @Query("SELECT COALESCE(SUM(r.tokensUsed),0) FROM RequestLog r " +
            "WHERE r.user.id = :userId " +
            "AND r.modelType = :modelType " +
            "AND r.timestamp >= :after")
    long sumTokensUsedByUserIdAndModelTypeAndTimestampAfter(
            Long userId,
            ModelType modelType,
            LocalDateTime after
    );

    long countByUserIdAndModelType(Long userId, ModelType modelType);

}

