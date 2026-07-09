package com.skillset.portal.dto;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.PositiveOrZero;

import lombok.Data;

import java.time.LocalDate;

import java.util.List;

@Data

public class EmployeeRegistrationDto {

    // Basic Details (Goes to Employee table)

    @NotBlank(message = "First name is required")

    private String firstName;

    @NotBlank(message = "Last name is required")

    private String lastName;

    @NotBlank(message = "Email is required")

    @Email(message = "Invalid email format")

    private String email;

    @NotBlank(message = "Password is required")

    private String password;

    @NotNull(message = "Mobile number is required")

    private Long mobile;

    @NotNull(message = "Date of joining is required")

    private LocalDate dateOfJoining;

    @NotNull(message = "Years of experience is required")

    @PositiveOrZero(message = "Experience cannot be negative")

    private Double yearOfExperience;

    private Integer reportingTo;

    // Goes to Role table

    @NotBlank(message = "Role is required")

    private String roleName;

    // Goes to Skill, Proficiency, and EmployeeSkill tables

    private String skillName;

    private String proficiencyName;

    private Double skillExperience;

    // Goes to Certification table

    private List<String> certifications;

    // Goes to Project table

    private List<String> projects;

}
