package com.skillset.portal.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Entity

@Table(name = "employee")

@Data

@NoArgsConstructor

@AllArgsConstructor

public class Employee {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer employeeId;

    private String firstName;

    private String lastName;

    private String email;

}