package org.ships.implementation.sponge.world.position.synced;

import org.core.entity.living.human.player.LivePlayer;
import org.core.exceptions.BlockNotSupported;
import org.core.vector.types.Vector3Int;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncPosition;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlags;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.world.position.SPosition;
import org.ships.implementation.sponge.world.position.block.details.blocks.StateDetails;
import org.ships.implementation.sponge.world.position.block.details.blocks.details.SBlockDetails;
import org.ships.implementation.sponge.world.position.block.details.blocks.snapshot.SBlockSnapshot;
import org.ships.implementation.sponge.world.position.flags.SApplyPhysicsFlag;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

public class SBlockPosition extends SSyncedPosition<Integer> implements SyncBlockPosition {

    public SBlockPosition(Location<World> location) {
        super(location, (Function<Location<World>, SPosition<Integer>>)(Object)SSyncedPosition.TO_SYNCED_BLOCK_POSITION);
    }

    @Override
    public Vector3Int getPosition() {
        return new Vector3Int(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ());
    }

    @Override
    public SBlockPosition setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        if(details instanceof SBlockSnapshot){
            SBlockSnapshot<? extends Position<Integer>> snapshot = (SBlockSnapshot<? extends Position<Integer>>) details;
            BlockSnapshot snapshot1 = snapshot.getSnapshot();
            snapshot1 = snapshot1.withLocation(this.location);
            SApplyPhysicsFlag physicsFlag = (SApplyPhysicsFlag) Stream.of(flags).filter(f -> f instanceof ApplyPhysicsFlag).findAny().orElse(ApplyPhysicsFlags.NONE);
            System.out.println("Snapshot restore: " + snapshot1.getState().getId());
            snapshot1.restore(true, physicsFlag.getFlag());
        }else {
            org.spongepowered.api.block.BlockState state = ((StateDetails) details).getState();
            SApplyPhysicsFlag physicsFlag = (SApplyPhysicsFlag) Stream.of(flags).filter(f -> f instanceof ApplyPhysicsFlag).findAny().orElse(ApplyPhysicsFlags.NONE);
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
        for(LivePlayer player : players){
            if(player.getPosition().getWorld().equals(this.getWorld())) {
                ((SLivePlayer) player).getSpongeEntity().sendBlockChange(this.location.getBlockPosition(), ((SBlockDetails)details).getState());
            }
        }
        return this;
    }

    @Override
    public SyncPosition<Integer> resetBlock(LivePlayer... players) {
        for(LivePlayer player : players){
            if(player.getPosition().getWorld().equals(this.getWorld())) {
                ((SLivePlayer) player).getSpongeEntity().resetBlockChange(this.location.getBlockPosition());
            }
        }
        return this;
    }
}