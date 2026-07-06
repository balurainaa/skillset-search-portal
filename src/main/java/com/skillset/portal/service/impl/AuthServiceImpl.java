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
import java.util.Map; // ---> ADDED THIS CRITICAL IMPORT TO FIX THE MAP COMPILATION ERROR

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
    public String login(LoginRequestDto loginDto) {
        Employee employee = employeeRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDto.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Clean out any old tokens for this employee before generating a new one
        userTokenRepository.deleteByEmployee_EmployeeId(employee.getEmployeeId());

        String token = jwtTokenProvider.generateToken(employee.getEmail());

        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setEmployee(employee);
        userToken.setExpiryDate(LocalDateTime.now().plusHours(8));

        userTokenRepository.save(userToken);

        return token;
    }

    @Override
    @Transactional
    public void register(Map<String, String> registerData) {
        String email = registerData.get("email");
        String password = registerData.get("password");
        String firstName = registerData.get("firstName");
        String lastName = registerData.get("lastName");

        // 1. Check if the email is already registered
        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email is already registered!");
        }

        // 2. Create and populate your Employee entity
        Employee employee = new Employee();
        employee.setEmail(email);

        // Encrypt the password before storing it
        employee.setPassword(passwordEncoder.encode(password));

        // Match these with whatever setter fields your Employee entity actually has:
        if(firstName != null) employee.setFirstName(firstName);
        if(lastName != null) employee.setLastName(lastName);

        // 3. Save the new employee to MySQL
        employeeRepository.save(employee);
    }
}