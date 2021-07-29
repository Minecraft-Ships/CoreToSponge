package org.ships.implementation.sponge.world.position.synced;

import org.core.CorePlugin;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.impl.sync.SyncPosition;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.position.SPosition;
import org.ships.implementation.sponge.world.position.block.details.blocks.details.SBlockDetails;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Arrays;
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
        BlockState state = ((SBlockDetails) details).getState();
        this.location.setBlock(state);
        return this;
    }

    @Override
    public SyncPosition<N> setBlock(BlockDetails details, LivePlayer... players) {
        BlockState state = ((SBlockDetails) details).getState();
        for (LivePlayer player : players) {
            ((SLivePlayer) player).getSpongeEntity().sendBlockChange(this.getX().intValue(), this.getY().intValue(), this.getZ().intValue(), state);
        }
        return this;
    }

    @Override
    public SSyncedPosition<N> resetBlock(LivePlayer... player) {
        Arrays
                .stream(player)
                .map(p -> ((SLivePlayer) p).getSpongeEntity())
                .filter(p -> p.world().equals(this.location.world()))
                .forEach(p -> p.resetBlockChange(this.location.blockPosition()));
        return this;
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        Optional<? extends BlockEntity> opSTileEntity = this.location.blockEntity();
        if (!opSTileEntity.isPresent()) {
            return Optional.empty();
        }
        return ((SpongePlatform) CorePlugin.getPlatform()).createTileEntityInstance(opSTileEntity.get());
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createEntity(EntityType<E, S> type) {
        return Optional.empty();
    }
}
