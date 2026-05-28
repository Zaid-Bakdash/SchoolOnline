package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.AuthRequest;
import com.bootcamp.onlineschool.dto.AuthResponse;
import com.bootcamp.onlineschool.dto.RegisterRequest;
import com.bootcamp.onlineschool.entity.AuthUser;
import com.bootcamp.onlineschool.entity.UserRole;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.AuthUserRepository;
import com.bootcamp.onlineschool.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthUserRepository authUserRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse register(RegisterRequest request) {
        validateRegisterRequest(request);

        if (authUserRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username already exists: " + request.getUsername());
        }
        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already exists: " + request.getEmail());
        }

        AuthUser authUser = new AuthUser();
        authUser.setName(request.getName());
        authUser.setUsername(request.getUsername());
        authUser.setEmail(request.getEmail());
        authUser.setPassword(passwordEncoder.encode(request.getPassword()));
        authUser.setRole(request.getRole() == null ? UserRole.USER : request.getRole());
        AuthUser savedUser = authUserRepository.save(authUser);

        String token = jwtTokenProvider.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(savedUser.getUsername())
                        .password(savedUser.getPassword())
                        .roles(savedUser.getRole().name())
                        .build()
        );

        return new AuthResponse(token, savedUser.getName(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            AuthUser authUser = authUserRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

            String token = jwtTokenProvider.generateToken(
                    org.springframework.security.core.userdetails.User.builder()
                            .username(authUser.getUsername())
                            .password(authUser.getPassword())
                            .roles(authUser.getRole().name())
                            .build()
            );

            return new AuthResponse(token, authUser.getName(), authUser.getUsername(), authUser.getEmail(), authUser.getRole());
        } catch (BadCredentialsException ex) {
            throw new ValidationException("Invalid username or password");
        }
    }

    public AuthResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("No authenticated user found");
        }

        String username = authentication.getName();
        AuthUser authUser = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Current user not found"));

        return new AuthResponse(null, authUser.getName(), authUser.getUsername(), authUser.getEmail(), authUser.getRole());
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new ValidationException("Registration request cannot be null");
        }
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (request.getRole() == null) {
            throw new ValidationException("Role is required");
        }
    }
}
