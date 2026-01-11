package com.pocketpipo.pocketpipo.controller;

import com.pocketpipo.pocketpipo.dto.CreateUserRequestDTO;
import com.pocketpipo.pocketpipo.dto.LoginRequestDTO;
import com.pocketpipo.pocketpipo.dto.LoginResponseDTO;
import com.pocketpipo.pocketpipo.security.JwtService;
import com.pocketpipo.pocketpipo.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authManager, JwtService jwtService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public void register(@RequestBody CreateUserRequestDTO dto) {
        userService.createUser(dto);
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
