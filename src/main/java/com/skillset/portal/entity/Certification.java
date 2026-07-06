package com.skillset.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer certificationId;

    @Column(nullable = false, length = 50)
    private String certificationName;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
