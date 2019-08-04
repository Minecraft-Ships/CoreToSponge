package org.ships.implementation.sponge.world;

import org.core.CorePlugin;
import org.core.entity.LiveEntity;
import org.core.world.WorldExtent;
import org.core.world.position.BlockPosition;
import org.core.world.position.ExactPosition;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.position.SBlockPosition;
import org.ships.implementation.sponge.world.position.SExactPosition;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SWorldExtent implements WorldExtent {

    protected org.spongepowered.api.world.World world;

    public SWorldExtent(org.spongepowered.api.world.World world){
        this.world = world;
    }

    public org.spongepowered.api.world.World getSpongeWorld(){
        return this.world;
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
        return new SExactPosition(this.world.getLocation(x, y, z));
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
    public Set<LiveEntity> getEntities() {
        Set<LiveEntity> set = new HashSet<>();
        SpongePlatform platform = ((SpongePlatform)CorePlugin.getPlatform());
        this.world.getEntities().forEach(e -> {
            LiveEntity entity = platform.createEntityInstance(e);
            set.add(entity);
        });
        return set;
    }
}
