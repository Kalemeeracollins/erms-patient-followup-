package com.example.backend.auth;

import com.example.backend.audit.AuditService;
import com.example.backend.auth.dto.LoginRequest;
import com.example.backend.auth.dto.LoginResponse;
import com.example.backend.auth.dto.RegisterRequest;
import com.example.backend.common.exception.BadRequestException;
import com.example.backend.security.JwtService;
import com.example.backend.user.Role;
import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication operations.
 * Handles user login, registration, token generation and validation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditService auditService;

    /**
     * Authenticate user and generate JWT tokens.
     * @param loginRequest login credentials
     * @return login response with JWT tokens
     * @throws BadRequestException if authentication fails
     */
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Get authenticated user
            User user = (User) authentication.getPrincipal();

            // Generate JWT tokens
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            log.info("User logged in successfully: {}", loginRequest.getUsername());
            auditService.logLoginSuccess(user.getId(), loginRequest.getUsername());

            // Build response
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getTokenExpiration() / 1000) // Convert to seconds
                    .user(LoginResponse.UserInfo.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .fullName(user.getFullName())
                            .email(user.getEmail())
                            .role(user.getRole().getCode())
                            .build())
                    .build();

        } catch (AuthenticationException e) {
            log.warn("Login failed for user: {}", loginRequest.getUsername());
            auditService.logLoginFailure(loginRequest.getUsername());
            throw new BadRequestException("Invalid username or password");
        }
    }

    /**
     * Register new user.
     * @param registerRequest registration data
     * @return login response with JWT tokens
     * @throws BadRequestException if registration fails
     */
    public LoginResponse register(RegisterRequest registerRequest) {
        log.info("Registration attempt for username: {}", registerRequest.getUsername());

        // Validate passwords match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            log.warn("Password mismatch for registration: {}", registerRequest.getUsername());
            throw new BadRequestException("Passwords do not match");
        }

        // Check if username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Username already exists: {}", registerRequest.getUsername());
            throw new BadRequestException("Username already exists");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Email already exists: {}", registerRequest.getEmail());
            throw new BadRequestException("Email already exists");
        }

        try {
            // Parse and validate role
            Role role = Role.valueOf(registerRequest.getRole().toUpperCase());

            // Create new user
            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .fullName(registerRequest.getFullName())
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(role)
                    .active(true)
                    .build();

            User savedUser = userRepository.save(user);
            log.info("User registered successfully: {}", registerRequest.getUsername());
            auditService.logUserCreation(savedUser.getId(), registerRequest.getUsername());

            // Generate tokens
            String accessToken = jwtService.generateToken(savedUser);
            String refreshToken = jwtService.generateRefreshToken(savedUser);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getTokenExpiration() / 1000)
                    .user(LoginResponse.UserInfo.builder()
                            .id(savedUser.getId())
                            .username(savedUser.getUsername())
                            .fullName(savedUser.getFullName())
                            .email(savedUser.getEmail())
                            .role(savedUser.getRole().getCode())
                            .build())
                    .build();

        } catch (IllegalArgumentException e) {
            log.warn("Invalid role provided: {}", registerRequest.getRole());
            throw new BadRequestException("Invalid role provided");
        }
    }

    /**
     * Refresh access token using refresh token.
     * @param refreshToken refresh token
     * @return new access token
     * @throws BadRequestException if token is invalid
     */
    public String refreshAccessToken(String refreshToken) {
        log.debug("Attempting to refresh access token");

        try {
            String username = jwtService.extractUsername(refreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (jwtService.isTokenValid(refreshToken, user)) {
                log.debug("Refresh token validated for user: {}", username);
                return jwtService.generateToken(user);
            }

            throw new BadRequestException("Refresh token is invalid or expired");

        } catch (Exception e) {
            log.error("Failed to refresh token: {}", e.getMessage());
            throw new BadRequestException("Failed to refresh token");
        }
    }
}
