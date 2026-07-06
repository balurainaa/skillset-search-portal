package com.skillset.portal.service.impl;

import com.skillset.portal.dto.LoginRequestDto;
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
    private UserTokenRepository userTokenRepository; // Uses your existing repository

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Uses your existing provider

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String login(LoginRequestDto loginDto) {
        Employee employee = employeeRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // If storing plain text passwords for testing, use: if(!loginDto.getPassword().equals(employee.getPassword()))
        if (!passwordEncoder.matches(loginDto.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Clean out any old tokens for this employee before generating a new one
        userTokenRepository.deleteByEmployee_EmployeeId(employee.getEmployeeId());

        String token = jwtTokenProvider.generateToken(employee.getEmail());

        // Uses your existing UserToken entity
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setEmployee(employee);
        userToken.setExpiryDate(LocalDateTime.now().plusHours(8));

        userTokenRepository.save(userToken);

        return token;
    }
}