package com.skillset.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfileDto {
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private Long mobile;
    private String roleName;
    private List<EmployeeSkillDto> skills;
    private List<String> projects;
    private List<String> certifications;
}