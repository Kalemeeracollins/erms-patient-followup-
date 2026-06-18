package com.example.backend.user;

/**
 * Enum representing user roles in the Hospital Patient Follow-Up System.
 * Each role has specific permissions and access levels.
 */
public enum Role {
    ADMIN("ADMIN", "System Administrator with full access"),
    DOCTOR("DOCTOR", "Doctor can view patients, create visits and follow-ups"),
    NURSE("NURSE", "Nurse can view patients and follow-ups"),
    RECEPTIONIST("RECEPTIONIST", "Receptionist can register and manage patient appointments");

    private final String code;
    private final String description;

    Role(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
