package com.skillset.portal.controller;

import com.skillset.portal.dto.AddProjectRequestDto;
import com.skillset.portal.dto.AddCertificationRequestDto;
import com.skillset.portal.dto.EmployeeRegistrationDto;
import com.skillset.portal.dto.EmployeeProfileDto;
import com.skillset.portal.service.EmployeeProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class EmployeeProfileController {

    @Autowired
    private EmployeeProfileService employeeProfileService;

    // 1. Unified Master Registration Endpoint
    // Accessible by both Roles since an employee completes registration details after auth setup
    @PostMapping("/submit")
    public ResponseEntity<String> submitProfile(@Valid @RequestBody EmployeeRegistrationDto dto) {
        // 💡 FIX: Pass null for the ID since it's a new registration row discovery
        String resultMessage = employeeProfileService.registerEmployee(null, dto);
        return ResponseEntity.ok(resultMessage);
    }

    // 2. Fetch Complete Aggregated Profile
    // Both Employees and Admins can view profile layouts
    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public EmployeeProfileDto getEmployeeProfile(@PathVariable Integer employeeId) {
        return employeeProfileService.getEmployeeProfile(employeeId);
    }

    // 3. EDITING: Admins or standard Employees can update profiles
    /*@PutMapping("/update/{employeeId}")
    //@PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isOwnProfile(authentication, #employeeId))")
    public ResponseEntity<String> updateProfile(@PathVariable Integer employeeId, @Valid @RequestBody EmployeeRegistrationDto dto) {
        String resultMessage = employeeProfileService.registerEmployee(dto);
        return ResponseEntity.ok(resultMessage);
    }*/

    @PutMapping("/update/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('EMPLOYEE') and principal.username == #pathVariable['employeeId'])")
    public ResponseEntity<String> updateProfile(@PathVariable("employeeId") Integer employeeId, @Valid @RequestBody EmployeeRegistrationDto dto) {

        // 💡 FIX: Add 'employeeId' as the first argument inside the parentheses here:
        String resultMessage = employeeProfileService.registerEmployee(employeeId, dto);

        return ResponseEntity.ok(resultMessage);
    }

    /*@PutMapping("/update/{employeeId}")

    @PreAuthorize("hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isOwnProfile(authentication, #employeeId))")

    public ResponseEntity<String> updateProfile(@PathVariable Integer employeeId, @Valid @RequestBody EmployeeRegistrationDto dto) {

        String response = employeeProfileService.updateEmployee(employeeId, dto);

        return ResponseEntity.ok(response);

    }*/

    // 4. DELETING: strictly locked to ADMIN users only
    @DeleteMapping("/delete/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProfile(@PathVariable Integer employeeId) {
        String resultMessage = employeeProfileService.deleteEmployeeProfile(employeeId);
        return ResponseEntity.ok(resultMessage);
    }

    // 5. Individual Component Actions (Legacy endpoints)

    // Only administrators can assign user organizational roles
    @PostMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignRole(@RequestParam Integer employeeId, @RequestParam String roleName) {
        return employeeProfileService.assignRole(employeeId, roleName);
    }

    // Employees and admins can add projects to profiles
    @PostMapping("/project")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public String addProject(@Valid @RequestBody AddProjectRequestDto projectRequest) {
        return employeeProfileService.addProject(projectRequest);
    }

    // Employees and admins can add certifications to profiles
    @PostMapping("/certification")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public String addCertification(@Valid @RequestBody AddCertificationRequestDto certRequest) {
        return employeeProfileService.addCertification(certRequest);
    }

    // Viewing sub-collections is viewable by both roles

    @GetMapping("/certifications/{employeeId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<String> getCertifications(@PathVariable Integer employeeId) {
        return employeeProfileService.getEmployeeCertifications(employeeId);
    }
}