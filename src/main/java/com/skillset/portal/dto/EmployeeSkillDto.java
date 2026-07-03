package com.skillset.portal.dto;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor

@AllArgsConstructor

public class EmployeeSkillDto {

    private String employeeName;

    private String skillName;

    private String proficiencyName;

    private Double yearsWorked;

}