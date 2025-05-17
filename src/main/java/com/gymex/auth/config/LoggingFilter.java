package com.gymex.auth.config;

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
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            logger.info("Authorization header: {}", authHeader);
        } else {
            logger.info("No Authorization header present.");
        }

        filterChain.doFilter(request, response);
    }
}
