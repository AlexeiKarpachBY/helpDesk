package com.training.akarpach.helpDesk.enums;

public enum Role {

    EMPLOYEE("Employee"),

    MANAGER("Manager"),

    ENGINEER("Engineer");

    private final String userRole;

    Role(String role) {
        this.userRole = role;
    }

    public String getUserRole() {
        return userRole;
    }

}
