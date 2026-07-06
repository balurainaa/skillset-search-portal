package com.skillset.portal.repository;

import com.skillset.portal.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Added this missing import statement

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
}