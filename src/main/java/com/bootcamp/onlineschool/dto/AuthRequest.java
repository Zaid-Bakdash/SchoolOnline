package com.bootcamp.onlineschool.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Authentication request with username and password")
public class AuthRequest {

    @Schema(description = "Username of the user", example = "admin", required = true)
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Password of the user", example = "password123", required = true)
    @NotBlank(message = "Password is required")
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
