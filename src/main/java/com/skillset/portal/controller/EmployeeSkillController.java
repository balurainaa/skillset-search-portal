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
public class EmployeeSkillController {

    @Autowired
    private EmployeeSkillService employeeSkillService;

    /*@PostMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('EMPLOYEE') and principal.username == #pathVariable['employeeId'])")
    public REmployeeSkillDto addSkill(
            @PathVariable("employeeSkillId") Integer employeeSkillId,
            @Valid @RequestBody AddSkillRequestDto requestDto) {

        EmployeeSkillDto updatedSkill = employeeSkillService.updateSkill(employeeSkillId, requestDto);
        return ResponseEntity.ok(updatedSkill);
    }*/

    @PostMapping
    public EmployeeSkillDto addSkill(@RequestBody AddSkillRequestDto requestDto) {

        return employeeSkillService.addSkill(requestDto);

    }

    @GetMapping("/search")
    public List<EmployeeSkillDto> searchBySkill(@RequestParam String skillName) {

        return employeeSkillService.searchBySkill(skillName);

    }

}
