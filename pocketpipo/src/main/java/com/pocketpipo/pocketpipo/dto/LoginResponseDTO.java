package com.pocketpipo.pocketpipo.dto;

public class LoginResponseDTO {
    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public LoginResponseDTO() { }
    public LoginResponseDTO(String token) { this.token = token; }
}

