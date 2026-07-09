package com.skillset.portal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        // Verify the token is present and valid
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

            // 1. Extract the unique identification email
            String email = tokenProvider.getEmailFromJWT(jwt);

            // 2. Extract the role from the token (Make sure this method exists in your JwtTokenProvider!)
            String role = tokenProvider.getRoleFromJWT(jwt);

            // 3. Convert the role string into a Spring Security GrantedAuthority format
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            // 4. Build the authentication context token with our new authorities/roles list
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email, null, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 5. Store it inside Spring Security's holder context so @PreAuthorize gates can see it
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Pass the request along to the next filter in the chain
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}