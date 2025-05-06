package org.dbp.hackaton.hackaton1.repository;

import org.dbp.hackaton.hackaton1.domain.Company;
import org.dbp.hackaton.hackaton1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByCompany(Company company);
}

