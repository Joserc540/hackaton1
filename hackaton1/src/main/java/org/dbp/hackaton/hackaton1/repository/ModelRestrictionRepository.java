package org.dbp.hackaton.hackaton1.repository;

import org.dbp.hackaton.hackaton1.domain.ModelRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRestrictionRepository extends JpaRepository<ModelRestriction, Long> {
    List<ModelRestriction> findByCompanyId(Long companyId);
}
