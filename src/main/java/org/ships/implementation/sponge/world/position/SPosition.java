package org.ships.implementation.sponge.world.position;

import com.flowpowered.math.vector.Vector3i;
import org.core.CorePlugin;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.vector.types.Vector3Int;
import org.core.world.WorldExtent;
import org.core.world.position.impl.sync.SyncPosition;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.entity.LiveTileEntity;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.SWorldExtent;
import org.ships.implementation.sponge.world.position.block.details.blocks.SBlockSnapshot;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public abstract class SPosition <N extends Number> implements SyncPosition<N> {

    protected Location<World> location;

    public SPosition(Location<World> location){
        this.location = location;
    }

    public Location<World> getSpongeLocation(){
        return this.location;
    }

    @Override
    public Vector3Int getChunkPosition() {
        Vector3i vector = this.location.getChunkPosition();
        return new Vector3Int(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public WorldExtent getWorld() {
        return new SWorldExtent(this.location.getExtent());
    }

    @Override
    public BlockSnapshot getBlockDetails() {
        return new SBlockSnapshot(this.location);
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        Optional<TileEntity> opSTileEntity = this.location.getTileEntity();
        if(!opSTileEntity.isPresent()){
            return Optional.empty();
        }
        LiveTileEntity lte = ((SpongePlatform)CorePlugin.getPlatform()).createTileEntityInstance(opSTileEntity.get()).get();
        return Optional.of(lte);
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createEntity(EntityType<E, S> type) {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof SyncPosition)){
            return false;
        }
        SyncPosition pos = (SyncPosition) obj;
        if(!pos.getWorld().equals(this.getWorld())){
            return false;
        }
        return pos.getPosition().equals(this.getPosition());
    }

    @Override
    public String toString(){
        return this.getX() + "," + this.getY() + "," + this.getZ() + "," + this.getWorld().getName();
    }
}
