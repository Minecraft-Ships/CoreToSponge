package org.ships.implementation.sponge.world.position.synced;

import org.core.CorePlugin;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.vector.Vector3;
import org.core.vector.types.Vector3Int;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncPosition;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.position.SPosition;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.function.Function;

public abstract class SSyncedPosition<N extends Number> extends SPosition<N> implements SyncPosition<N> {

    public SSyncedPosition(Location<World> location, Function<Location<World>, SPosition<N>> newInstance) {
        super(location, newInstance);
    }

    @Override
    public SyncPosition<N> destroy() {
        //TODO
        return this;
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        Optional<TileEntity> opSTileEntity = this.location.getTileEntity();
        if(!opSTileEntity.isPresent()){
            return Optional.empty();
        }
        LiveTileEntity lte = ((SpongePlatform) CorePlugin.getPlatform()).createTileEntityInstance(opSTileEntity.get()).get();
        return Optional.of(lte);
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createEntity(EntityType<E, S> type) {
        return Optional.empty();
    }
}
