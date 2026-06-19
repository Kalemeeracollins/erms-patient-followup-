package com.example.backend.auth;

import com.example.backend.auth.dto.LoginRequest;
import com.example.backend.auth.dto.LoginResponse;
import com.example.backend.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for authentication operations.
 * Handles login, registration, and token refresh endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, 
             allowCredentials = "true", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    /**
     * User login endpoint.
     * POST /api/auth/login
     * @param loginRequest login credentials
     * @return login response with JWT tokens
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request received for user: {}", loginRequest.getUsername());
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * User registration endpoint.
     * POST /api/auth/register
     * @param registerRequest registration data
     * @return login response with JWT tokens for new user
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Registration request received for username: {}", registerRequest.getUsername());
        LoginResponse response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Refresh access token endpoint.
     * POST /api/auth/refresh
     * @param request map containing refreshToken
     * @return new access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        log.debug("Token refresh request received");

        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Refresh token is required");
            return ResponseEntity.badRequest().body(error);
        }

        String newAccessToken = authService.refreshAccessToken(refreshToken);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("tokenType", "Bearer");

        return ResponseEntity.ok(response);
    }

    /**
     * Get current authenticated user information.
     * GET /api/auth/me
     * @return current user information
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Map<String, Object> user = new HashMap<>();
        user.put("username", authentication.getName());
        user.put("authorities", authentication.getAuthorities());

        return ResponseEntity.ok(user);
    }

    /**
     * Logout endpoint (optional - mainly for frontend).
     * POST /api/auth/logout
     * @return logout response
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("Logout request received");
        SecurityContextHolder.clearContext();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");

        return ResponseEntity.ok(response);
    }
}
