package com.skillset.portal.service;

import com.skillset.portal.dto.AddSkillRequestDto;

import com.skillset.portal.dto.EmployeeSkillDto;

import java.util.List;

public interface EmployeeSkillService {

    EmployeeSkillDto addSkill(AddSkillRequestDto requestDto);

    List<EmployeeSkillDto> searchBySkill(String skillName);

}
