package com.skillset.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Generates Getters, Setters, toString(), equals(), and hashCode()
@NoArgsConstructor // Generates the default no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields (useful for testing)
public class RegisterRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastname;
}
