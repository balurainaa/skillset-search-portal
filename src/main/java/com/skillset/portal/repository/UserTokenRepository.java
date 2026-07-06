package com.skillset.portal.repository;

import com.skillset.portal.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByToken(String token);
    void deleteByEmployee_EmployeeId(Integer employeeId);
}
