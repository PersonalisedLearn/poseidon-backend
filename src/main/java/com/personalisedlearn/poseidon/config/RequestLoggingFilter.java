package com.personalisedlearn.poseidon.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        
        // Log the incoming request
        logger.info("Incoming Request: {} {}", request.getMethod(), request.getRequestURI());
        logger.debug("Request Headers: {}", getRequestHeadersAsString(request));
        
        try {
            // Continue with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Log the response status and time taken
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Request Completed: {} {} - Status: {} ({} ms)",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
        }
    }

    private String getRequestHeadersAsString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n")
        );
        return headers.toString();
    }
}
