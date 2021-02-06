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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.client.LocalPlayer;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.world.dimension.DimensionTypes;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.*;

public class SWorldExtent implements WorldExtent {

    protected org.spongepowered.api.world.World<?> world;

    public SWorldExtent(org.spongepowered.api.world.World<?> world){
        this.world = world;
    }

    public org.spongepowered.api.world.World<?> getSpongeWorld(){
        return this.world;
    }

    @Override
    public String getName() {
        if(this.world instanceof ServerWorld){
            return ((ServerWorld)this.world).getKey().getValue();
        }
        if(this.world.getDimensionType().equals(DimensionTypes.OVERWORLD.get())){
            return "World";
        }
        if(this.world.getDimensionType().equals(DimensionTypes.THE_END.get())){
            return "The_End";
        }
        if(this.world.getDimensionType().equals(DimensionTypes.THE_NETHER.get())){
            return "The_Nether";
        }
        throw new IllegalStateException("Unknown World name of " + this.world.getDimensionType().getKey().asString() + " | " + this.world.getSeed());
    }

    @Override
    public UUID getUniqueId() {
        if(this.world instanceof ServerWorld) {
            return ((ServerWorld)this.world).getUniqueId();
        }
        throw new IllegalStateException("UUID not accessible via client");
    }

    @Override
    public String getPlatformUniqueId() {
        if(this.world instanceof ServerWorld){
            return ((ServerWorld)this.world).getKey().asString();
        }
        if(this.world.getDimensionType().equals(DimensionTypes.OVERWORLD.get())){
            return "World";
        }
        if(this.world.getDimensionType().equals(DimensionTypes.THE_END.get())){
            return "The_End";
        }
        if(this.world.getDimensionType().equals(DimensionTypes.THE_NETHER.get())){
            return "The_Nether";
        }
        throw new IllegalStateException("Unknown World name of " + this.world.getDimensionType().getKey().asString() + " | " + this.world.getSeed());
    }

    @Override
    public SyncExactPosition getPosition(double x, double y, double z) {
        return new SExactPosition(this.world.getLocation(x, y, z));
    }

    @Override
    public ASyncExactPosition getAsyncPosition(double x, double y, double z) {
        throw new IllegalStateException("ASync position not used in Sponge yet");
    }

    @Override
    public SyncBlockPosition getPosition(int x, int y, int z) {
        return new SBlockPosition(this.world.getLocation(x, y, z));
    }

    @Override
    public ASyncBlockPosition getAsyncPosition(int x, int y, int z) {
        throw new IllegalStateException("ASync position not used in Sponge yet");
    }

    @Override
    public boolean isLoaded() {
        return this.world.isLoaded();
    }

    @Override
    public Set<LiveEntity> getEntities() {
        Set<LiveEntity> set = new HashSet<>();
        SpongePlatform platform = ((SpongePlatform)CorePlugin.getPlatform());
        if(this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).getEntities().forEach(e -> {
                LiveEntity entity = platform.createEntityInstance(e);
                set.add(entity);
            });
        }else{
            Optional<LocalPlayer> opPlayer = Sponge.getClient().getPlayer();
            if(!opPlayer.isPresent()){
                return Collections.emptySet();
            }
            this.world.getEntities(opPlayer.get().getBoundingBox().get());
        }
        return set;
    }

    @Override
    public Set<LiveTileEntity> getTileEntities() {
        Set<LiveTileEntity> set = new HashSet<>();
        this.world.getBlockEntities().forEach(te -> ((SpongePlatform)CorePlugin.getPlatform()).createTileEntityInstance(te).ifPresent(set::add));
        return set;
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof WorldExtent)){
            return false;
        }
        WorldExtent extent = (WorldExtent) object;
        return extent.getPlatformUniqueId().equals(this.getPlatformUniqueId());
    }
}
