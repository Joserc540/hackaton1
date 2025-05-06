package org.dbp.hackaton.hackaton1.repository;

import org.dbp.hackaton.hackaton1.domain.Company;
import org.dbp.hackaton.hackaton1.domain.ModelType;
import org.dbp.hackaton.hackaton1.domain.RequestLog;
import org.dbp.hackaton.hackaton1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
    List<RequestLog> findByUser(User user);
    List<RequestLog> findByCompany(Company company);
    List<RequestLog> findByUserAndModel(User user, ModelType model);
}

