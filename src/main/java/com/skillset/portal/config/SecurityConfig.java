package com.skillset.portal.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class SecurityConfig {

    @Bean

    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails admin = User.builder()

                .username("admin")

                .password(encoder.encode("admin123"))

                .roles("ADMIN")

                .build();

        return new InMemoryUserDetailsManager(admin);

    }

    @Bean

    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/skills/search", "/api/profile/projects/**", "/api/profile/certifications/**").permitAll()

                        .anyRequest().authenticated()

                )

                .httpBasic(basic -> {});

        return http.build();

    }

}
