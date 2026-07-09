package com.skillset.portal.repository;

import com.skillset.portal.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByEmployee_EmployeeId(Integer employeeId);

    void deleteByEmployee_EmployeeId(Integer employeeId);
}