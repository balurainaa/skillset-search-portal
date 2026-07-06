package com.skillset.portal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    private String firstName;
    private String lastName;
    private String email;
    private Long mobile;
    private LocalDate dateOfJoining;
    private Double yearOfExperience;
    private Integer reportingTo;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}