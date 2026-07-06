package com.skillset.portal.service.impl;

import com.skillset.portal.dto.AddProjectRequestDto;
import com.skillset.portal.dto.AddCertificationRequestDto;
import com.skillset.portal.dto.EmployeeProfileDto;
import com.skillset.portal.dto.EmployeeRegistrationDto;
import com.skillset.portal.entity.*;
import com.skillset.portal.repository.*;
import com.skillset.portal.service.EmployeeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    @Override
    public String assignRole(Integer employeeId, String roleName) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Role role = roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(roleName);
                    return roleRepository.save(newRole);
                });

        employee.setRole(role);
        employeeRepository.save(employee);
        return "Role '" + roleName + "' successfully assigned to employee ID: " + employeeId;
    }

    @Override
    public String addProject(AddProjectRequestDto projectDto) {
        Employee employee = employeeRepository.findById(projectDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setEmployee(employee);
        projectRepository.save(project);

        return "Project '" + projectDto.getProjectName() + "' added to employee profile.";
    }

    @Override
    public String addCertification(AddCertificationRequestDto certDto) {
        Employee employee = employeeRepository.findById(certDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Certification certification = new Certification();
        certification.setCertificationName(certDto.getCertificationName());
        certification.setEmployee(employee);
        certificationRepository.save(certification);

        return "Certification '" + certDto.getCertificationName() + "' recorded for employee.";
    }

    @Override
    public List<String> getEmployeeProjects(Integer employeeId) {
        return projectRepository.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(Project::getProjectName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getEmployeeCertifications(Integer employeeId) {
        return certificationRepository.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(Certification::getCertificationName)
                .collect(Collectors.toList());
    }

    @Override
    public String registerEmployee(EmployeeRegistrationDto registrationDto) {
        return "";
    }

    @Override
    public EmployeeProfileDto getEmployeeProfile(Integer employeeId) {
        return null;
    }
}
