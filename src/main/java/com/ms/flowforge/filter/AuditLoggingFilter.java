package com.ms.flowforge.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuditLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        
        try {
            // Proceed with the next filter or target controller
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String clientIp = request.getRemoteAddr();

            logger.info("AUDIT - method={} uri={} status={} clientIp={} duration={}ms",
                    method, uri, status, clientIp, duration);
        }
    }
}
