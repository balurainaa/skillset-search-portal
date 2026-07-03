package com.skillset.portal.repository;

import com.skillset.portal.entity.Skill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

    Optional<Skill> findBySkillName(String skillName);

}