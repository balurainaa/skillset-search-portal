package com.skillset.portal.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Entity

@Table(name = "employee_skill")

@Data

@NoArgsConstructor

@AllArgsConstructor

public class EmployeeSkill {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    @ManyToOne

    @JoinColumn(name = "employee_id")

    private Employee employee;

    @ManyToOne

    @JoinColumn(name = "skill_id")

    private Skill skill;

    @ManyToOne

    @JoinColumn(name = "proficiency_id")

    private Proficiency proficiency;

    private Double yearsWorked;

}
