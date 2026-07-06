package com.skillset.portal.service;

import com.skillset.portal.dto.LoginRequestDto;

public interface AuthService {
    String login(LoginRequestDto loginDto);
}