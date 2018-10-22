package org.ships.implementation.sponge.world.position;

import com.flowpowered.math.vector.Vector3i;
import org.core.CorePlugin;
import org.core.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.vector.types.Vector3Int;
import org.core.world.WorldExtent;
import org.core.world.position.Position;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.entity.LiveTileEntity;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.SWorldExtent;
import org.ships.implementation.sponge.world.position.block.details.blocks.AbstractBlockDetails;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public abstract class SPosition <N extends Number> implements Position<N> {

    protected Location<World> location;

    public SPosition(Location<World> location){
        this.location = location;
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
    public BlockDetails getBlockDetails() {
        return ((SpongePlatform) CorePlugin.getPlatform()).createBlockDetailsInstance(this.location.getBlock()).get();
    }

    @Override
    public Position<N> setBlock(BlockDetails details) {
        org.spongepowered.api.block.BlockState state = ((AbstractBlockDetails)details).getBlockstate();
        this.location.setBlock(state);
        return this;
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        return Optional.empty();
    }

    @Override
    public <E extends Entity, S extends EntitySnapshot<E>> Optional<S> createEntity(EntityType<E, S> type) {
        return Optional.empty();
    }
}
