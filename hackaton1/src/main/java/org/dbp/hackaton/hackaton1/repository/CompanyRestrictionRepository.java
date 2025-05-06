package org.dbp.hackaton.hackaton1.repository;

import org.dbp.hackaton.hackaton1.domain.Company;
import org.dbp.hackaton.hackaton1.domain.CompanyRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRestrictionRepository extends JpaRepository<CompanyRestriction, Long> {
    List<CompanyRestriction> findByCompany_Id(Long companyId);
    List<CompanyRestriction> findByCompany(Company company);
}

