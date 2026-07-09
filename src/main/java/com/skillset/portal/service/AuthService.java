package com.skillset.portal.service;

import com.skillset.portal.dto.LoginRequestDto;
import com.skillset.portal.dto.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginDto);

}