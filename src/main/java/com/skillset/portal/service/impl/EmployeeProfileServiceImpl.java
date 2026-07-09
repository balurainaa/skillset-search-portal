package com.skillset.portal.service.impl;

import com.skillset.portal.dto.AddProjectRequestDto;
import com.skillset.portal.dto.AddCertificationRequestDto;
import com.skillset.portal.dto.EmployeeProfileDto;
import com.skillset.portal.dto.EmployeeRegistrationDto;
import com.skillset.portal.dto.EmployeeSkillDto;
import com.skillset.portal.entity.*;
import com.skillset.portal.repository.*;
import com.skillset.portal.service.EmployeeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProficiencyRepository proficiencyRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Override
    @Transactional
    public String registerEmployee(Integer employeeId, EmployeeRegistrationDto registrationDto) {

        Employee employee;


        // 1. Get the true, verified email of the logged-in user from their token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName(); // This is the username/email from the JWT

        // 💡 FIX: Split logic securely
        if (employeeId == null) {
            // Handle POST /submit:
            // 🛡️ SECURITY FIX: Ignore the email in the JSON body! Look up by the token email instead.
            employee = employeeRepository.findByEmail(loggedInUserEmail)
                    .orElseThrow(() -> new RuntimeException("Account not found for logged-in user: " + loggedInUserEmail));
        } else {
            // Handle PUT /update/{employeeId}: Look up by URL ID
            employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Account with ID " + employeeId + " not found."));

            // 🛡️ SECURITY CHECK: Ensure the logged-in user owns this specific profile ID
            if (!employee.getEmail().equalsIgnoreCase(loggedInUserEmail)) {
                throw new RuntimeException("Access Denied! You cannot modify another user's account details.");
            }
        }

        Role role = roleRepository.findByRoleName(registrationDto.getRoleName())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(registrationDto.getRoleName());
                    return roleRepository.save(newRole);
                });
        // --- Keep the rest of your mapping code exactly the same ---
        employee.setFirstName(registrationDto.getFirstName());
        employee.setLastName(registrationDto.getLastName());
        employee.setMobile(registrationDto.getMobile());
        employee.setDateOfJoining(registrationDto.getDateOfJoining());
        employee.setYearOfExperience(registrationDto.getYearOfExperience());
        employee.setReportingTo(registrationDto.getReportingTo());
        employee.setRole(role);

        employee = employeeRepository.save(employee);
        return "Employee profile processed successfully for ID: " + employee.getEmployeeId();
    }

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

    // --- UPDATED WORKFLOW: PREVENTS DUPLICATING DATABASE ROWS ---
    // --- UPDATED WORKFLOW: PREVENTS DUPLICATING DATABASE ROWS ---
    /*@Override
    @Transactional
    public String registerEmployee(Integer employeeId, EmployeeRegistrationDto registrationDto) { // 💡 Added employeeId parameter

        // 1. Fetch the targeted employee row by the URL ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Account with ID " + employeeId + " not found."));

        // 2. 🛡️ SECURITY CHECK: Ensure the email in the DTO matches the database row owner
        if (!employee.getEmail().equalsIgnoreCase(registrationDto.getEmail())) {
            throw new RuntimeException("Email mismatch! You cannot modify another user's account details.");
        }

        // 3. Fetch or create the Role
        Role role = roleRepository.findByRoleName(registrationDto.getRoleName())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(registrationDto.getRoleName());
                    return roleRepository.save(newRole);
                });

        // 4. Map the update fields safely
        employee.setFirstName(registrationDto.getFirstName());
        employee.setLastName(registrationDto.getLastName());
        employee.setMobile(registrationDto.getMobile());
        employee.setDateOfJoining(registrationDto.getDateOfJoining());
        employee.setYearOfExperience(registrationDto.getYearOfExperience());
        employee.setReportingTo(registrationDto.getReportingTo());
        employee.setRole(role);

        // 5. Save the updated entity
        employee = employeeRepository.save(employee);

        return "Employee profile updated successfully for ID: " + employee.getEmployeeId();
    }*/

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

    @Override
    @Transactional
    public String deleteEmployeeProfile(Integer employeeId) {
        // 1. Verify the employee exists
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        // 2. MANUALLY CLEAR CHILD RECORDS FIRST to satisfy the MySQL constraint
        // (Inject these repositories at the top of your file if you haven't already)
        employeeSkillRepository.deleteByEmployee_EmployeeId(employeeId);
        projectRepository.deleteByEmployee_EmployeeId(employeeId);
        certificationRepository.deleteByEmployee_EmployeeId(employeeId);

        // 3. Now it is safe to delete the parent employee row!
        employeeRepository.delete(employee);

        return "Employee profile and all associated data with ID: " + employeeId + " deleted successfully.";
    }
}