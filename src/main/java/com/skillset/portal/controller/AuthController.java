package com.skillset.portal.controller;

import com.skillset.portal.dto.LoginRequestDto;
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

    // --- YOUR EXISTING LOGIN METHOD ---
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto loginDto) {
        String token = authService.login(loginDto);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");

        return ResponseEntity.ok(response);
    }

    // --- THE MISSING REGISTER METHOD YOU NEED TO ADD ---
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> registerData) {
        Map<String, String> response = new HashMap<>();

        try {
            // Call the service to encrypt and save the user
            authService.register(registerData);

            response.put("message", "User registered successfully!");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Handles errors like "Email is already registered!"
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}