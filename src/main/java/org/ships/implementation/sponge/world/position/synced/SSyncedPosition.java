package org.ships.implementation.sponge.world.position.synced;

import org.core.CorePlugin;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.vector.type.Vector3;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.impl.sync.SyncPosition;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.position.SPosition;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Optional;
import java.util.function.Function;

public abstract class SSyncedPosition<N extends Number> extends SPosition<N> implements SyncPosition<N> {

    public SSyncedPosition(Location<? extends World<?, ?>, ?> location, Function<? extends Location<? extends World<?, ?>, ?>, ? extends SPosition<N>> newInstance) {
        super(location, newInstance);
    }

    @Override
    public SyncPosition<N> destroy() {
        World<?, ?> world = this.location.world();
        if (!(world instanceof ServerWorld)) {
            return this;
        }
        ServerWorld sWorld = (ServerWorld) world;
        sWorld.destroyBlock(this.location.blockPosition(), true);
        return this;
    }

    @Override
    public SyncPosition<N> setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        return null;
    }

    @Override
    public SyncPosition<N> setBlock(BlockDetails details, LivePlayer... player) {
        return null;
    }

    @Override
    public SyncPosition<N> resetBlock(LivePlayer... player) {
        return null;
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        Optional<? extends BlockEntity> opSTileEntity = this.location.blockEntity();
        if (!opSTileEntity.isPresent()) {
            return Optional.empty();
        }
        LiveTileEntity lte = ((SpongePlatform) CorePlugin.getPlatform()).createTileEntityInstance(opSTileEntity.get()).get();
        return Optional.of(lte);
    }

    @Override
    public Vector3<N> getPosition() {
        return null;
    }

    @Override
    public SyncPosition<N> getRelative(Vector3<?> vector) {
        return null;
    }

    @Override
    public SyncPosition<N> getRelative(Direction direction) {
        return null;
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createEntity(EntityType<E, S> type) {
        return Optional.empty();
    }
}
