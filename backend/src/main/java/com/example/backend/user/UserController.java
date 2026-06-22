package com.example.backend.user;

import com.example.backend.user.dto.CreateUserRequest;
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

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"},
             allowCredentials = "true", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        log.info("Fetching user with username: {}", username);
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        log.info("Admin creating user: {}", request.getUsername());
        UserDTO created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, String> updateRequest) {
        log.info("Updating user with ID: {}", id);

        String fullName = updateRequest.get("fullName");
        String email = updateRequest.get("email");
        String phoneNumber = updateRequest.get("phoneNumber");
        String role = updateRequest.get("role");

        UserDTO updatedUser = userService.updateUser(id, fullName, email, phoneNumber, role);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> activateUser(@PathVariable Long id) {
        log.info("Activating user with ID: {}", id);
        return ResponseEntity.ok(userService.setUserActive(id, true));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable Long id) {
        log.info("Deactivating user with ID: {}", id);
        return ResponseEntity.ok(userService.setUserActive(id, false));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.warn("Deleting user with ID: {}", id);
        userService.deleteUser(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("userId", String.valueOf(id));
        return ResponseEntity.ok(response);
    }
}
