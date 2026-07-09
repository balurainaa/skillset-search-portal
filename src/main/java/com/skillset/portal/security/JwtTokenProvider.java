package com.skillset.portal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // A fixed signing key for learning/local development so it doesn't change on application restart
    private final String SECRET_KEY = "secretKeyForLearningJWTInSpringBootMustBeLongEnough";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final long jwtExpirationInMs = 86400000; // Token lasts exactly 24 Hours

    // Generates a token containing both the email and the role claim mapping
    public String generateToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);

        // Ensure we don't double-prefix if the role string already comes with "ROLE_"
        if (role != null && !role.startsWith("ROLE_")) {
            claims.put("role", "ROLE_" + role.toUpperCase());
        } else if (role != null) {
            claims.put("role", role.toUpperCase());
        } else {
            claims.put("role", "ROLE_EMPLOYEE"); // Fallback safety default
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key, SignatureAlgorithm.HS256) // FIXED: Uses the matching secure cryptographic key object
                .compact();
    }

    // Extracts the unique identification email out of a token string
    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // ADDED: Extracts the role claim string so the JwtAuthenticationFilter can see user privileges
    public String getRoleFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Fetches the custom "role" claim value we stored during generation
        return claims.get("role", String.class);
    }

    // Basic validator: Verifies the signature isn't broken or expired
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}