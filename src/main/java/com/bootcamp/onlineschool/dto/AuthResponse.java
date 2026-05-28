package com.bootcamp.onlineschool.dto;

import com.bootcamp.onlineschool.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response with token and user metadata")
public class AuthResponse {

    @Schema(description = "JWT token returned after successful authentication")
    private String token;

    @Schema(description = "Full name of the authenticated user")
    private String name;

    @Schema(description = "Username of the authenticated user")
    private String username;

    @Schema(description = "Email of the authenticated user")
    private String email;

    @Schema(description = "Role assigned to the authenticated user")
    private UserRole role;

    public AuthResponse() {
    }

    public AuthResponse(String token, String name, String username, String email, UserRole role) {
        this.token = token;
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
