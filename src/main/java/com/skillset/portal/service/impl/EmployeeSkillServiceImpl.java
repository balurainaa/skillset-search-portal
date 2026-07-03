package com.skillset.portal.service.impl;

import com.skillset.portal.dto.AddSkillRequestDto;

import com.skillset.portal.dto.EmployeeSkillDto;

import com.skillset.portal.entity.*;

import com.skillset.portal.repository.*;

import com.skillset.portal.service.EmployeeSkillService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service

public class EmployeeSkillServiceImpl implements EmployeeSkillService {

    @Autowired

    private EmployeeRepository employeeRepository;

    @Autowired

    private SkillRepository skillRepository;

    @Autowired

    private ProficiencyRepository proficiencyRepository;

    @Autowired

    private EmployeeSkillRepository employeeSkillRepository;

    @Override

    public EmployeeSkillDto addSkill(AddSkillRequestDto requestDto) {

        Employee employee = employeeRepository.findById(requestDto.getEmployeeId())

                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Skill skill = skillRepository.findBySkillName(requestDto.getSkillName())

                .orElseGet(() -> {

                    Skill newSkill = new Skill();

                    newSkill.setSkillName(requestDto.getSkillName());

                    return skillRepository.save(newSkill);

                });

        Proficiency proficiency = proficiencyRepository.findByName(requestDto.getProficiencyName())

                .orElseGet(() -> {

                    Proficiency newProf = new Proficiency();

                    newProf.setName(requestDto.getProficiencyName());

                    return proficiencyRepository.save(newProf);

                });

        EmployeeSkill employeeSkill = new EmployeeSkill();

        employeeSkill.setEmployee(employee);

        employeeSkill.setSkill(skill);

        employeeSkill.setProficiency(proficiency);

        employeeSkill.setYearsWorked(requestDto.getYearsWorked());

        EmployeeSkill saved = employeeSkillRepository.save(employeeSkill);

        return toDto(saved);

    }

    @Override

    public List<EmployeeSkillDto> searchBySkill(String skillName) {

        List<EmployeeSkill> results = employeeSkillRepository.findBySkill_SkillName(skillName);

        return results.stream().map(this::toDto).collect(Collectors.toList());

    }

    private EmployeeSkillDto toDto(EmployeeSkill es) {

        String fullName = es.getEmployee().getFirstName() + " " + es.getEmployee().getLastName();

        return new EmployeeSkillDto(fullName, es.getSkill().getSkillName(),

                es.getProficiency().getName(), es.getYearsWorked());

    }

}
