package org.ships.implementation.sponge.world.position.synced;

import org.core.entity.living.human.player.LivePlayer;
import org.core.exceptions.BlockNotSupported;
import org.core.vector.type.Vector3;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlags;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.position.impl.sync.SyncPosition;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.world.position.block.details.blocks.StateDetails;
import org.ships.implementation.sponge.world.position.block.details.blocks.details.SBlockDetails;
import org.ships.implementation.sponge.world.position.block.details.blocks.snapshot.SBlockSnapshot;
import org.ships.implementation.sponge.world.position.flags.SApplyPhysicsFlag;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class SBlockPosition extends SSyncedPosition<Integer> implements SyncBlockPosition {

    public SBlockPosition(Location<? extends World<?, ?>, ?> location) {
        super(location, SSyncedPosition.TO_SYNCED_BLOCK_POSITION);
    }

    @Override
    public SBlockPosition getRelative(Direction direction) {
        Vector3<Integer> vector = direction.getAsVector();
        return new SBlockPosition(this.location.add(vector.getX(), vector.getY(), vector.getZ()));
    }

    @Override
    public SBlockPosition getRelative(Vector3<?> vector) {
        return new SBlockPosition(this.location.add(vector.getX().doubleValue(), vector.getY().doubleValue(), vector.getZ().doubleValue()));
    }

    @Override
    public SBlockSnapshot<SyncBlockPosition> getBlockDetails() {
        return new SBlockSnapshot<>(this.location.onServer().orElseThrow(() -> new IllegalStateException("Client isn't supported yet")), (Function<Location<? extends World<?, ?>, ?>, SyncBlockPosition>) (Object) this.newInstance);
    }

    @Override
    public Vector3<Integer> getPosition() {
        return Vector3.valueOf(this.location.blockX(), this.location.blockY(), this.location.blockZ());
    }

    @Override
    public SBlockPosition setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        Optional<ServerLocation> opLocSer = this.location.onServer();
        if (details instanceof SBlockSnapshot && opLocSer.isPresent()) {
            SBlockSnapshot<? extends Position<Integer>> snapshot = (SBlockSnapshot<? extends Position<Integer>>) details;
            BlockSnapshot snapshot1 = snapshot.getSnapshot();
            snapshot1 = snapshot1.withLocation(opLocSer.get());
            SApplyPhysicsFlag physicsFlag = (SApplyPhysicsFlag) Stream.of(flags).filter(f -> f instanceof ApplyPhysicsFlag).findAny().orElse(ApplyPhysicsFlags.NONE.get());
            snapshot1.restore(true, physicsFlag.getFlag());
        } else {
            org.spongepowered.api.block.BlockState state = ((StateDetails) details).getState();
            SApplyPhysicsFlag physicsFlag = (SApplyPhysicsFlag) Stream.of(flags).filter(f -> f instanceof ApplyPhysicsFlag).findAny().orElse(ApplyPhysicsFlags.NONE.get());
            this.location.setBlock(state, physicsFlag.getFlag());
            if (details.get(KeyedData.TILED_ENTITY).isPresent()) {
                TileEntitySnapshot<? extends TileEntity> snapshot = details.get(KeyedData.TILED_ENTITY).get();
                try {
                    snapshot.apply(this);
                } catch (BlockNotSupported blockNotSupported) {
                    blockNotSupported.printStackTrace();
                }
            }
        }
        return this;
    }

    @Override
    public SyncPosition<Integer> setBlock(BlockDetails details, LivePlayer... players) {
        for (LivePlayer player : players) {
            if (player.getPosition().getWorld().equals(this.getWorld())) {
                ((SLivePlayer) player).getSpongeEntity().sendBlockChange(this.location.blockPosition(), ((SBlockDetails) details).getState());
            }
        }
        return this;
    }

    @Override
    public SyncExactPosition toExactPosition() {
        return new SExactPosition(this.location);
    }
}
