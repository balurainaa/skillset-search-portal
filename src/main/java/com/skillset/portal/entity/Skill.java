package com.skillset.portal.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Entity

@Table(name = "skill")

@Data

@NoArgsConstructor

@AllArgsConstructor

public class Skill {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer skillId;

    private String skillName;

}