package com.skillset.portal.service.impl;

import com.skillset.portal.dto.AddProjectRequestDto;
import com.skillset.portal.dto.AddCertificationRequestDto;
import com.skillset.portal.dto.EmployeeProfileDto;
import com.skillset.portal.dto.EmployeeRegistrationDto;
import com.skillset.portal.dto.EmployeeSkillDto; // Added for structural mapping
import com.skillset.portal.entity.*;
import com.skillset.portal.repository.*;
import com.skillset.portal.service.EmployeeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for database consistency

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

    // --- ADDED INJECTIONS TO SUPPORT NEW METHODS ---
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProficiencyRepository proficiencyRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

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

    // --- COMPLETED METHODS TARGETING THE NEW REGISTRATION PROFILE WORKFLOW ---
    @Override
    @Transactional
    public String registerEmployee(EmployeeRegistrationDto registrationDto) {
        // 1. Fetch or create Role
        Role role = roleRepository.findByRoleName(registrationDto.getRoleName())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(registrationDto.getRoleName());
                    return roleRepository.save(newRole);
                });

        // 2. Build core employee entity
        Employee employee = new Employee();
        employee.setFirstName(registrationDto.getFirstName());
        employee.setLastName(registrationDto.getLastName());
        employee.setEmail(registrationDto.getEmail());
        employee.setMobile(registrationDto.getMobile());
        employee.setDateOfJoining(registrationDto.getDateOfJoining());
        employee.setYearOfExperience(registrationDto.getYearOfExperience());
        employee.setReportingTo(registrationDto.getReportingTo());
        employee.setRole(role);

        employee = employeeRepository.save(employee);

        // 3. Associate skill data metrics if populated
        if (registrationDto.getSkillName() != null && !registrationDto.getSkillName().trim().isEmpty()) {
            Skill skill = skillRepository.findBySkillName(registrationDto.getSkillName())
                    .orElseGet(() -> {
                        Skill newSkill = new Skill();
                        newSkill.setSkillName(registrationDto.getSkillName());
                        return skillRepository.save(newSkill);
                    });

            Proficiency proficiency = proficiencyRepository.findByName(registrationDto.getProficiencyName())
                    .orElseGet(() -> {
                        Proficiency newProf = new Proficiency();
                        newProf.setName(registrationDto.getProficiencyName());
                        return proficiencyRepository.save(newProf);
                    });

            EmployeeSkill empSkill = new EmployeeSkill();
            empSkill.setEmployee(employee);
            empSkill.setSkill(skill);
            empSkill.setProficiency(proficiency);
            empSkill.setYearsWorked(registrationDto.getSkillExperience());

            employeeSkillRepository.save(empSkill);
        }

        // 4. Save list collections of projects
        if (registrationDto.getProjects() != null) {
            for (String projName : registrationDto.getProjects()) {
                if (!projName.trim().isEmpty()) {
                    Project project = new Project();
                    project.setProjectName(projName);
                    project.setEmployee(employee);
                    projectRepository.save(project);
                }
            }
        }

        // 5. Save list collections of certifications
        if (registrationDto.getCertifications() != null) {
            for (String certName : registrationDto.getCertifications()) {
                if (!certName.trim().isEmpty()) {
                    Certification cert = new Certification();
                    cert.setCertificationName(certName);
                    cert.setEmployee(employee);
                    certificationRepository.save(cert);
                }
            }
        }

        return "Employee registered successfully with ID: " + employee.getEmployeeId();
    }

    @Override
    public EmployeeProfileDto getEmployeeProfile(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<EmployeeSkillDto> skillDtos = employeeSkillRepository.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(es -> new EmployeeSkillDto(
                        employee.getFirstName() + " " + employee.getLastName(),
                        es.getSkill().getSkillName(),
                        es.getProficiency().getName(),
                        es.getYearsWorked()
                ))
                .collect(Collectors.toList());

        List<String> projects = getEmployeeProjects(employeeId);
        List<String> certifications = getEmployeeCertifications(employeeId);

        return new EmployeeProfileDto(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getMobile(),
                employee.getRole() != null ? employee.getRole().getRoleName() : "N/A",
                skillDtos,
                projects,
                certifications
        );
    }
}