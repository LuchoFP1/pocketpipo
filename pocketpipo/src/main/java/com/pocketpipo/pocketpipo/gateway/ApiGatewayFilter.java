package com.pocketpipo.pocketpipo.gateway;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
public class ApiGatewayFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String CORRELATION_ID = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        String correlationId = request.getHeader(CORRELATION_ID);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        response.setHeader(CORRELATION_ID, correlationId);


        String path = request.getRequestURI();
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || authHeader.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            response.getWriter().write("""
                {
                  "timestamp": "%s",
                  "status": 401,
                  "error": "UNAUTHORIZED",
                  "message": "Missing Authorization header",
                  "path": "%s"
                }
                """.formatted(Instant.now().toString(), path));
            return;
        }

        
        filterChain.doFilter(request, response);
    }
}