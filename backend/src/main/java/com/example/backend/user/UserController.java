package com.example.backend.user;

import com.example.backend.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for user management operations.
 * Handles user CRUD operations with role-based authorization.
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, 
             allowCredentials = "true", maxAge = 3600)
public class UserController {

    private final UserService userService;

    /**
     * Get all users.
     * GET /api/users
     * @return list of all users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID.
     * GET /api/users/{id}
     * @param id user ID
     * @return user information
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by username.
     * GET /api/users/username/{username}
     * @param username username
     * @return user information
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        log.info("Fetching user with username: {}", username);
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Update user information.
     * PUT /api/users/{id}
     * @param id user ID
     * @param updateRequest map containing fields to update
     * @return updated user information
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, String> updateRequest) {
        log.info("Updating user with ID: {}", id);

        String fullName = updateRequest.get("fullName");
        String email = updateRequest.get("email");
        String phoneNumber = updateRequest.get("phoneNumber");

        UserDTO updatedUser = userService.updateUser(id, fullName, email, phoneNumber);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Activate user.
     * PUT /api/users/{id}/activate
     * @param id user ID
     * @return updated user information
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> activateUser(@PathVariable Long id) {
        log.info("Activating user with ID: {}", id);
        UserDTO updatedUser = userService.setUserActive(id, true);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deactivate user.
     * PUT /api/users/{id}/deactivate
     * @param id user ID
     * @return updated user information
     */
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable Long id) {
        log.info("Deactivating user with ID: {}", id);
        UserDTO updatedUser = userService.setUserActive(id, false);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete user.
     * DELETE /api/users/{id}
     * @param id user ID
     * @return deletion confirmation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.warn("Deleting user with ID: {}", id);
        
        // Get user to verify it exists
        userService.getUserById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deletion would be implemented here");
        response.put("userId", String.valueOf(id));

        return ResponseEntity.ok(response);
    }
}
