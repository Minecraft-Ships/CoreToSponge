package org.core.implementation.sponge.entity.living.human.player.live;

import org.core.entity.living.human.player.User;

import java.util.UUID;

public class SUser implements User {

    protected final org.spongepowered.api.entity.living.player.User user;

    public SUser(org.spongepowered.api.entity.living.player.User user) {
        this.user = user;
    }

    public org.spongepowered.api.entity.living.player.User getUser() {
        return this.user;
    }

    @Override
    public String getName() {
        return this.user.name();
    }

    @Override
    public UUID getUniqueId() {
        return this.user.uniqueId();
    }
}
