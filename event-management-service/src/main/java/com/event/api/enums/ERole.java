package com.event.api.enums;

public enum ERole {

    ROLE_USER(101, "ROLE_USER"),
    ROLE_MODERATOR(102, "ROLE_HOST"),
    ROLE_ADMIN(103, "ROLE_ADMIN");

    private int id;
    private String name;

    ERole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
