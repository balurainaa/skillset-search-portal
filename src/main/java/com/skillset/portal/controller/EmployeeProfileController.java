package com.skillset.portal.controller;

import com.skillset.portal.dto.AddProjectRequestDto;
import com.skillset.portal.dto.AddCertificationRequestDto;
import com.skillset.portal.dto.EmployeeRegistrationDto;
import com.skillset.portal.dto.EmployeeProfileDto;
import com.skillset.portal.service.EmployeeProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Added missing import
import org.springframework.security.access.prepost.PreAuthorize; // Added missing import
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class EmployeeProfileController {

    @Autowired
    private EmployeeProfileService employeeProfileService;

    // 1. Unified Master Registration Endpoint (Returns ResponseEntity cleanly)
    @PostMapping("/submit")
    public ResponseEntity<String> registerEmployee(@Valid @RequestBody EmployeeRegistrationDto registrationDto) {
        String response = employeeProfileService.registerEmployee(registrationDto);
        return ResponseEntity.ok(response);
    }

    // 2. Fetch Complete Aggregated Profile (The Profile GET View)
    @GetMapping("/{employeeId}")
    public EmployeeProfileDto getEmployeeProfile(@PathVariable Integer employeeId) {
        return employeeProfileService.getEmployeeProfile(employeeId);
    }

    // 3. EDITING: Admins can update any ID; Standard Employees can ONLY update their own matching ID
    @PutMapping("/update/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isOwnProfile(authentication, #employeeId))")
    public ResponseEntity<String> updateProfile(@PathVariable Integer employeeId, @Valid @RequestBody EmployeeRegistrationDto dto) {
        // Your logic to update the profile details will be called here
        return ResponseEntity.ok("Profile updated successfully");
    }

    // 4. DELETING: Admins only
    @DeleteMapping("/delete/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProfile(@PathVariable Integer employeeId) {
        // Your logic to delete the profile will be called here
        return ResponseEntity.ok("Employee profile deleted successfully by Admin");
    }

    // 5. Individual Component Actions (Legacy endpoints)
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