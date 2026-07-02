package com.skillset.portal.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Entity

@Table(name = "proficiency")

@Data

@NoArgsConstructor

@AllArgsConstructor

public class Proficiency {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer proficiencyId;

    private String name;

}