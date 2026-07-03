package com.skillset.portal.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data

public class AddSkillRequestDto {

    @NotNull(message = "Employee ID is required")

    private Integer employeeId;

    @NotBlank(message = "Skill name is required")

    private String skillName;

    @NotBlank(message = "Proficiency is required")

    private String proficiencyName;

    @Positive(message = "Years worked must be greater than zero")

    private Double yearsWorked;

}