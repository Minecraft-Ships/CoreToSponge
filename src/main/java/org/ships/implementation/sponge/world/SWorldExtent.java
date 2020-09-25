package org.ships.implementation.sponge.world;

import org.core.CorePlugin;
import org.core.entity.LiveEntity;
import org.core.world.WorldExtent;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.position.block.entity.LiveTileEntity;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;
import org.ships.implementation.sponge.world.position.synced.SExactPosition;

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
    public SyncExactPosition getPosition(double x, double y, double z) {
        return new SExactPosition(this.world.getLocation(x, y, z));
    }

    @Override
    public ASyncExactPosition getAsyncPosition(double x, double y, double z) {
        return null;
    }

    @Override
    public SyncBlockPosition getPosition(int x, int y, int z) {
        return new SBlockPosition(this.world.getLocation(x, y, z));
    }

    @Override
    public ASyncBlockPosition getAsyncPosition(int x, int y, int z) {
        return null;
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

    @Override
    public Set<LiveTileEntity> getTileEntities() {
        Set<LiveTileEntity> set = new HashSet<>();
        this.world.getTileEntities().stream().forEach(te -> {
            ((SpongePlatform)CorePlugin.getPlatform()).createTileEntityInstance(te).ifPresent(set::add);
        });
        return set;
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof WorldExtent)){
            return false;
        }
        WorldExtent extent = (WorldExtent) object;
        return extent.getUniquieId().equals(this.getUniquieId());
    }
}