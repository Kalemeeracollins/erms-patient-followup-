package com.example.backend.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for audit logging operations.
 * Logs important events like login, logout, user creation, and password changes.
 */
@Slf4j
@Service
public class AuditService {

    /**
     * Log successful login.
     * @param userId user ID
     * @param username username
     */
    public void logLoginSuccess(Long userId, String username) {
        log.info("AUDIT - LOGIN_SUCCESS: userId={}, username={}", userId, username);
    }

    /**
     * Log failed login.
     * @param username username
     */
    public void logLoginFailure(String username) {
        log.warn("AUDIT - LOGIN_FAILURE: username={}", username);
    }

    /**
     * Log logout.
     * @param userId user ID
     * @param username username
     */
    public void logLogout(Long userId, String username) {
        log.info("AUDIT - LOGOUT: userId={}, username={}", userId, username);
    }

    /**
     * Log user creation.
     * @param userId user ID
     * @param username username
     */
    public void logUserCreation(Long userId, String username) {
        log.info("AUDIT - USER_CREATED: userId={}, username={}", userId, username);
    }

    /**
     * Log user update.
     * @param userId user ID
     * @param fullName updated full name
     * @param email updated email
     */
    public void logUserUpdate(Long userId, String fullName, String email) {
        log.info("AUDIT - USER_UPDATED: userId={}, fullName={}, email={}", userId, fullName, email);
    }

    /**
     * Log user status change (activation/deactivation).
     * @param userId user ID
     * @param active activation status
     */
    public void logUserStatusChange(Long userId, boolean active) {
        String status = active ? "ACTIVATED" : "DEACTIVATED";
        log.info("AUDIT - USER_{}: userId={}", status, userId);
    }

    /**
     * Log password change.
     * @param userId user ID
     * @param username username
     */
    public void logPasswordChange(Long userId, String username) {
        log.info("AUDIT - PASSWORD_CHANGED: userId={}, username={}", userId, username);
    }

    /**
     * Log unauthorized access attempt.
     * @param username username
     * @param resource resource accessed
     */
    public void logUnauthorizedAccess(String username, String resource) {
        log.warn("AUDIT - UNAUTHORIZED_ACCESS: username={}, resource={}", username, resource);
    }
}
