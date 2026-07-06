package com.skillset.portal.service;

import com.skillset.portal.dto.AddProjectRequestDto;
import com.skillset.portal.dto.AddCertificationRequestDto;
import java.util.List;

public interface EmployeeProfileService {
    String assignRole(Integer employeeId, String roleName);
    String addProject(AddProjectRequestDto projectDto);
    String addCertification(AddCertificationRequestDto certDto);
    List<String> getEmployeeProjects(Integer employeeId);
    List<String> getEmployeeCertifications(Integer employeeId);
}