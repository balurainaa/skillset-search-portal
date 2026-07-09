package com.skillset.portal.controller;

import com.skillset.portal.dto.LoginRequestDto;

import com.skillset.portal.dto.LoginResponseDto;

import com.skillset.portal.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import java.util.Map;

@RestController

@RequestMapping("/api/auth")

public class AuthController {

    @Autowired

    private AuthService authService;

    @PostMapping("/login")

    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginDto) {

        LoginResponseDto loginResponse = authService.login(loginDto);

        Map<String, Object> response = new HashMap<>();

        response.put("token", loginResponse.getToken());

        response.put("type", "Bearer");

        response.put("roleName", loginResponse.getRoleName());

        response.put("employeeId", loginResponse.getEmployeeId());

        return ResponseEntity.ok(response);

    }

}