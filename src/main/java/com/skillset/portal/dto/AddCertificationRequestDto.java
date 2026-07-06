package com.skillset.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCertificationRequestDto {
    @NotNull(message = "Employee ID is required")
    private Integer employeeId;

    @NotBlank(message = "Certification name is required")
    private String certificationName;
}
