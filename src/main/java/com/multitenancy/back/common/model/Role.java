package com.multitenancy.back.common.model;

public enum Role {
    USER("USER", "user", 1),
    ADMIN("ADMIN", "admin", 3);

    private final String userRole;
    private final int roleType;

    Role(String role, String prefix, int roleType) {
        this.userRole = role;
        this.roleType = roleType;
    }

    public String role() {
        return userRole;
    }

    public static boolean isUser(int type) {
        return type == USER.roleType;
    }

    public static boolean isAdmin(int type) { return type == ADMIN.roleType; }

}
