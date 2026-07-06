package com.skillset.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProjectRequestDto {
    @NotNull(message = "Employee ID is required")
    private Integer employeeId;

    @NotBlank(message = "Project name is required")
    private String projectName;
}