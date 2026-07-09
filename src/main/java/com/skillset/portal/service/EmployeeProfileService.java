package com.skillset.portal.service;

import com.skillset.portal.dto.AddProjectRequestDto;

import com.skillset.portal.dto.AddCertificationRequestDto;

import com.skillset.portal.dto.EmployeeProfileDto;

import com.skillset.portal.dto.EmployeeRegistrationDto;

import java.util.List;

public interface EmployeeProfileService {

    String assignRole(Integer employeeId, String roleName);

    String addProject(AddProjectRequestDto projectDto);

    String addCertification(AddCertificationRequestDto certDto);

    List<String> getEmployeeProjects(Integer employeeId);

    List<String> getEmployeeCertifications(Integer employeeId);

    String registerEmployee(EmployeeRegistrationDto registrationDto);

    EmployeeProfileDto getEmployeeProfile(Integer employeeId);

    String updateEmployee(Integer employeeId, EmployeeRegistrationDto registrationDto);

    String deleteEmployee(Integer employeeId);

}