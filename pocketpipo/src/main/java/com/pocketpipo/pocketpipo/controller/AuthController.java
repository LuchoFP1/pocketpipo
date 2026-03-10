package com.pocketpipo.pocketpipo.controller;

import com.pocketpipo.pocketpipo.dto.LoginRequestDTO;
import com.pocketpipo.pocketpipo.dto.LoginResponseDTO;
import com.pocketpipo.pocketpipo.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

          
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
    try {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
    } catch (org.springframework.security.core.AuthenticationException ex) {
        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED,
                ex.getClass().getSimpleName() + ": " + ex.getMessage()
        );
    }

    String token = jwtService.generateToken(dto.getEmail());
    return new LoginResponseDTO(token);
    }
}
