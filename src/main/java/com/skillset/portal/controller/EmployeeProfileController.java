package com.skillset.portal.controller;

import com.skillset.portal.dto.AddProjectRequestDto;
import com.skillset.portal.dto.AddCertificationRequestDto;
import com.skillset.portal.dto.EmployeeRegistrationDto;
import com.skillset.portal.dto.EmployeeProfileDto;
import com.skillset.portal.service.EmployeeProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class EmployeeProfileController {

    @Autowired
    private EmployeeProfileService employeeProfileService;

    // 1. Unified Master Registration Endpoint (The Page 3 Form Submission)
    @PostMapping("/submit")
    public String registerEmployee(@Valid @RequestBody EmployeeRegistrationDto registrationDto) {
        return employeeProfileService.registerEmployee(registrationDto);
    }

    // 2. Fetch Complete Aggregated Profile (The Profile GET View)
    @GetMapping("/{employeeId}")
    public EmployeeProfileDto getEmployeeProfile(@PathVariable Integer employeeId) {
        return employeeProfileService.getEmployeeProfile(employeeId);
    }

    // 3. Individual Component Actions (From the previous code)
    @PostMapping("/role")
    public String assignRole(@RequestParam Integer employeeId, @RequestParam String roleName) {
        return employeeProfileService.assignRole(employeeId, roleName);
    }

    @PostMapping("/project")
    public String addProject(@Valid @RequestBody AddProjectRequestDto projectRequest) {
        return employeeProfileService.addProject(projectRequest);
    }

    @PostMapping("/certification")
    public String addCertification(@Valid @RequestBody AddCertificationRequestDto certRequest) {
        return employeeProfileService.addCertification(certRequest);
    }

    @GetMapping("/projects/{employeeId}")
    public List<String> getProjects(@PathVariable Integer employeeId) {
        return employeeProfileService.getEmployeeProjects(employeeId);
    }

    @GetMapping("/certifications/{employeeId}")
    public List<String> getCertifications(@PathVariable Integer employeeId) {
        return employeeProfileService.getEmployeeCertifications(employeeId);
    }
}