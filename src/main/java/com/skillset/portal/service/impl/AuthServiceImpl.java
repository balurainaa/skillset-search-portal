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
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Using this instance!

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(Map<String, String> registerData) {
        String email = registerData.get("email");
        String password = registerData.get("password");
        String firstName = registerData.get("firstName");
        String lastName = registerData.get("lastName");

        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email is already registered!");
        }

        Employee employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));

        if(firstName != null) employee.setFirstName(firstName);
        if(lastName != null) employee.setLastName(lastName);

        employeeRepository.save(employee);
    }
    @Override
    @Transactional
    public String login(LoginRequestDto loginDto) {
        Employee employee = employeeRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDto.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Clean, direct token generation using just the user's email address
        // Inside AuthServiceImpl.java login method
        String roleName = employee.getRole() != null ? employee.getRole().getRoleName() : "EMPLOYEE";
        String token = jwtTokenProvider.generateToken(employee.getEmail(), roleName);
        return token;
    }


}