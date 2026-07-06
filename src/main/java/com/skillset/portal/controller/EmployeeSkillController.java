package com.skillset.portal.controller;

import com.skillset.portal.dto.AddSkillRequestDto;

import com.skillset.portal.dto.EmployeeSkillDto;

import com.skillset.portal.service.EmployeeSkillService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/skills")
@CrossOrigin(origins = " *")
public class EmployeeSkillController {

    @Autowired

    private EmployeeSkillService employeeSkillService;

    @PostMapping

    public EmployeeSkillDto addSkill(@Valid @RequestBody AddSkillRequestDto requestDto) {

        return employeeSkillService.addSkill(requestDto);

    }

    @GetMapping("/search")

    public List<EmployeeSkillDto> searchBySkill(@RequestParam String skillName) {

        return employeeSkillService.searchBySkill(skillName);

    }

}
