package com.ms.flowforge.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Simple bypass for development (no real JWT validation yet)
        String path = request.getRequestURI();

        // Allow public endpoints
        if (path.startsWith("/api/auth") || path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        // For all other requests, add dummy authentication for now
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("user", null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}