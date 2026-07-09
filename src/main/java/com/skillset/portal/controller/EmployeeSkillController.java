package com.skillset.portal.controller;

import com.skillset.portal.dto.AddSkillRequestDto;

import com.skillset.portal.dto.EmployeeSkillDto;

import com.skillset.portal.service.EmployeeSkillService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/skills")
public class EmployeeSkillController {

    @Autowired

    private EmployeeSkillService employeeSkillService;

    @PostMapping
   @PreAuthorize("hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isOwnProfile(authentication, #requestDto.employeeId))")

    public EmployeeSkillDto addSkill(@Valid @RequestBody AddSkillRequestDto requestDto) {

        return employeeSkillService.addSkill(requestDto);

    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmployeeSkillDto> searchBySkill(@RequestParam String skillName) {

        return employeeSkillService.searchBySkill(skillName);

    }

}
