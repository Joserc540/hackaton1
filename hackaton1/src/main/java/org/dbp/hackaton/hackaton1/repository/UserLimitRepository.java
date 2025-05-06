package org.dbp.hackaton.hackaton1.repository;

import org.dbp.hackaton.hackaton1.domain.ModelType;
import org.dbp.hackaton.hackaton1.domain.User;
import org.dbp.hackaton.hackaton1.domain.UserLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLimitRepository extends JpaRepository<UserLimit, Long> {
    List<UserLimit> findByUser(User user);
    Optional<UserLimit> findByUserAndModel(User user, ModelType model);
}

