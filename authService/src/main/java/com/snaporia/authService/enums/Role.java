package com.snaporia.authService.enums;

import lombok.Data;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(Permission.USER_READ, Permission.USER_WRITE, Permission.USER_UPDATE, Permission.USER_DELETE)),
    USER(Set.of(Permission.USER_READ, Permission.USER_WRITE)),
    GUEST(Set.of());
    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
