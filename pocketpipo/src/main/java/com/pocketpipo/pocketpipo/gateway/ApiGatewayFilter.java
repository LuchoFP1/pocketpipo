package com.pocketpipo.pocketpipo.gateway;

import com.pocketpipo.pocketpipo.security.CustomUserDetailsService;
import com.pocketpipo.pocketpipo.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
public class ApiGatewayFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String MDC_CORRELATION_ID_KEY = "correlationId";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public ApiGatewayFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1) Correlation-Id (header in/out)
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        // 2) Put into MDC so every log in this request can print it
        MDC.put(MDC_CORRELATION_ID_KEY, correlationId);
        try {
            String path = request.getRequestURI();

            // 3) Bypass auth endpoints
            if (path.startsWith("/auth")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 4) Read + validate Authorization header
            String authHeader = request.getHeader(AUTHORIZATION);
            if (authHeader == null || authHeader.isBlank()) {
                writeUnauthorized(response, path, "Missing Authorization header");
                return;
            }

            if (!authHeader.startsWith("Bearer ")) {
                writeUnauthorized(response, path, "Invalid Authorization header format (expected Bearer token)");
                return;
            }

            String token = authHeader.substring(7).trim();
            if (token.isEmpty()) {
                writeUnauthorized(response, path, "Missing Bearer token");
                return;
            }

            // 5) Validate token + set SecurityContext
            try {
                String email = jwtService.extractSubject(token);
                if (email == null || email.isBlank()) {
                    writeUnauthorized(response, path, "Invalid token subject");
                    return;
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    if (!jwtService.isTokenValid(token, userDetails)) {
                        writeUnauthorized(response, path, "Invalid or expired token");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

                // 6) Continue the chain only when everything is OK
                filterChain.doFilter(request, response);

            } catch (Exception ex) {
                writeUnauthorized(response, path, "Invalid or expired token");
            }

        } finally {
            MDC.remove(MDC_CORRELATION_ID_KEY);
        }
    }

    private void writeUnauthorized(HttpServletResponse response, String path, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write("""
            {
              "timestamp": "%s",
              "status": 401,
              "error": "UNAUTHORIZED",
              "message": "%s",
              "path": "%s"
            }
            """.formatted(Instant.now().toString(), message, path));
    }
}