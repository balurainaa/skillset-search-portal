package com.skillset.portal.security;

import com.skillset.portal.entity.Employee;
import com.skillset.portal.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public boolean isOwnProfile(Authentication authentication, Integer employeeId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String loggedInEmail = authentication.getName(); // Extracted from JWT principal
        Employee targetEmployee = employeeRepository.findById(employeeId).orElse(null);

        if (targetEmployee == null) {
            return false;
        }

        return loggedInEmail.equalsIgnoreCase(targetEmployee.getEmail());
    }
}
