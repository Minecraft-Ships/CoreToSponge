package org.core.implementation.sponge.platform;

import org.core.permission.Permission;

public class SPermission implements Permission {

    private final String permission;

    public SPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermissionValue() {
        return this.permission;
    }


}
