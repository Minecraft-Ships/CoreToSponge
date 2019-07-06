package org.ships.implementation.sponge.entity.living.human.player.live;

import org.core.entity.living.human.player.User;

import java.util.UUID;

public class SUser implements User {

    protected org.spongepowered.api.entity.living.player.User user;

    public SUser(org.spongepowered.api.entity.living.player.User user){
        this.user = user;
    }

    @Override
    public String getName() {
        return this.user.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.user.getUniqueId();
    }
}
