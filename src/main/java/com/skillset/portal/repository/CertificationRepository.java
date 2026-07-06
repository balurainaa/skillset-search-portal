package com.skillset.portal.repository;

import com.skillset.portal.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    List<Certification> findByEmployee_EmployeeId(Integer employeeId);
}