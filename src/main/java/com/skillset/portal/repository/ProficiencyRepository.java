package com.skillset.portal.repository;

import com.skillset.portal.entity.Proficiency;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProficiencyRepository extends JpaRepository<Proficiency, Integer> {

    Optional<Proficiency> findByName(String name);

}