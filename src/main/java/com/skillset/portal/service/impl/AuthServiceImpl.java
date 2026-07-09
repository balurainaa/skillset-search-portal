package com.skillset.portal.service.impl;

import com.skillset.portal.dto.LoginRequestDto;

import com.skillset.portal.dto.LoginResponseDto;

import com.skillset.portal.entity.Employee;

import com.skillset.portal.entity.UserToken;

import com.skillset.portal.repository.EmployeeRepository;

import com.skillset.portal.repository.UserTokenRepository;

import com.skillset.portal.security.JwtTokenProvider;

import com.skillset.portal.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service

public class AuthServiceImpl implements AuthService {

    @Autowired

    private EmployeeRepository employeeRepository;

    @Autowired

    private UserTokenRepository userTokenRepository;

    @Autowired

    private JwtTokenProvider jwtTokenProvider;

    @Autowired

    private PasswordEncoder passwordEncoder;

    @Override

    @Transactional

    public LoginResponseDto login(LoginRequestDto loginDto) {

        Employee employee = employeeRepository.findByEmail(loginDto.getEmail())

                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDto.getPassword(), employee.getPassword())) {

            throw new RuntimeException("Invalid email or password");

        }

        userTokenRepository.deleteByEmployee_EmployeeId(employee.getEmployeeId());

        String token = jwtTokenProvider.generateToken(employee.getEmail());

        UserToken userToken = new UserToken();

        userToken.setToken(token);

        userToken.setEmployee(employee);

        userToken.setExpiryDate(LocalDateTime.now().plusHours(8));

        userTokenRepository.save(userToken);

        String roleName = employee.getRole() != null ? employee.getRole().getRoleName() : null;

        return new LoginResponseDto(token, roleName, employee.getEmployeeId());

    }

}
