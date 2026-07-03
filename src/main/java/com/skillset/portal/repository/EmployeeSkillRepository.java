package com.skillset.portal.repository;

import com.skillset.portal.entity.EmployeeSkill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Integer> {

    List<EmployeeSkill> findBySkill_SkillName(String skillName);

}
