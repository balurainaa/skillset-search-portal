package com.skillset.portal.service;

import com.skillset.portal.dto.LoginRequestDto;
import com.skillset.portal.dto.RegisterRequestDto;

import java.util.Map;

public interface AuthService {
    String login(LoginRequestDto loginDto);

    // Add this line:
    void register(RegisterRequestDto registerData);
}