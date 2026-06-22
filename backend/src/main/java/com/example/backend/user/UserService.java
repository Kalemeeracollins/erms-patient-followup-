package com.example.backend.user;

import com.example.backend.audit.AuditService;
import com.example.backend.user.Role;
import com.example.backend.common.exception.ResourceNotFoundException;
import com.example.backend.common.exception.BadRequestException;
import com.example.backend.user.dto.CreateUserRequest;
import com.example.backend.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for user management operations.
 * Handles user creation, retrieval, update, and deletion.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    /**
     * Get all users.
     * @return list of UserDTOs
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get user by ID.
     * @param id user ID
     * @return UserDTO
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });
        return convertToDTO(user);
    }

    /**
     * Get user by username.
     * @param username username
     * @return UserDTO
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        log.debug("Fetching user with username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new ResourceNotFoundException("User not found with username: " + username);
                });
        return convertToDTO(user);
    }

    /**
     * Update user information.
     * @param id user ID
     * @param fullName new full name
     * @param email new email
     * @param phoneNumber new phone number
     * @return updated UserDTO
     * @throws ResourceNotFoundException if user not found
     */
    public UserDTO updateUser(Long id, String fullName, String email, String phoneNumber, String role) {
        log.debug("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        if (email != null && !email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
            log.warn("Email already in use: {}", email);
            throw new BadRequestException("Email already in use");
        }

        if (fullName != null && !fullName.isBlank()) {
            user.setFullName(fullName);
        }
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber.isBlank() ? null : phoneNumber);
        }
        if (role != null && !role.isBlank()) {
            try {
                user.setRole(Role.valueOf(role.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + role);
            }
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", id);
        auditService.logUserUpdate(id, fullName, email);

        return convertToDTO(updatedUser);
    }

    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + request.getRole());
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .active(true)
                .build();

        User saved = userRepository.save(user);
        auditService.logUserCreation(saved.getId(), saved.getUsername());
        log.info("Admin created user: {}", saved.getUsername());
        return convertToDTO(saved);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        userRepository.delete(user);
        log.info("User deleted: ID={}", id);
    }

    /**
     * Activate or deactivate user.
     * @param id user ID
     * @param active activation status
     * @return updated UserDTO
     * @throws ResourceNotFoundException if user not found
     */
    public UserDTO setUserActive(Long id, boolean active) {
        log.debug("Setting user {} status to: {}", id, active);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setActive(active);
        User updatedUser = userRepository.save(user);

        log.info("User active status updated: ID={}, active={}", id, active);
        auditService.logUserStatusChange(id, active);

        return convertToDTO(updatedUser);
    }

    /**
     * Convert User entity to UserDTO.
     * @param user User entity
     * @return UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().getCode())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Check if user exists by username.
     * @param username username to check
     * @return true if user exists
     */
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if user exists by email.
     * @param email email to check
     * @return true if user exists
     */
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
