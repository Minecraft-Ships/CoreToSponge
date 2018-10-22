package org.ships.implementation.sponge.world;

import org.core.entity.Entity;
import org.core.world.WorldExtent;
import org.core.world.position.BlockPosition;
import org.core.world.position.ExactPosition;
import org.ships.implementation.sponge.world.position.SBlockPosition;

import java.util.Set;
import java.util.UUID;

public class SWorldExtent implements WorldExtent {

    org.spongepowered.api.world.World world;

    public SWorldExtent(org.spongepowered.api.world.World world){
        this.world = world;
    }

    @Override
    public String getName() {
        return this.world.getName();
    }

    @Override
    public UUID getUniquieId() {
        return this.world.getUniqueId();
    }

    @Override
    public String getPlatformUniquieId() {
        return getUniquieId().toString();
    }

    @Override
    public ExactPosition getPosition(double x, double y, double z) {
        return null;
    }

    @Override
    public BlockPosition getPosition(int x, int y, int z) {
        return new SBlockPosition(this.world.getLocation(x, y, z));
    }

    @Override
    public boolean isLoaded() {
        return this.world.isLoaded();
    }

    @Override
    public Set<Entity> getEntities() {
        return null;
    }
}
