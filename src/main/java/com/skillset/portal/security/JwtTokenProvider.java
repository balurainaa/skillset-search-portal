package com.skillset.portal.security;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Decoders;

import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.security.Key;

import java.util.Date;

@Component

public class JwtTokenProvider {

    @Value("${jwt.secret}")

    private String jwtSecret;

    private final long jwtExpirationInMs = 28800000; // 8 Hours

    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String generateToken(String email) {

        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()

                .setSubject(email)

                .setIssuedAt(new Date())

                .setExpiration(expiryDate)

                .signWith(getSigningKey())

                .compact();

    }

    public String getEmailFromJWT(String token) {

        Claims claims = Jwts.parserBuilder()

                .setSigningKey(getSigningKey())

                .build()

                .parseClaimsJws(token)

                .getBody();

        return claims.getSubject();

    }

    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);

            return true;

        } catch (Exception ex) {

            return false;

        }

    }

}