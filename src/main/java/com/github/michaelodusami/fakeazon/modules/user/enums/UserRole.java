package com.github.michaelodusami.fakeazon.modules.user.enums;

public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getRole().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("No role found for: " + role);
    }
}
