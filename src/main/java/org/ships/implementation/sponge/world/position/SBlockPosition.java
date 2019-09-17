package org.ships.implementation.sponge.world.position;

import org.core.entity.living.human.player.LivePlayer;
import org.core.exceptions.BlockNotSupported;
import org.core.vector.types.Vector3Int;
import org.core.world.position.BlockPosition;
import org.core.world.position.Position;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlags;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.world.position.block.details.blocks.SBlockDetails;
import org.ships.implementation.sponge.world.position.flags.SApplyPhysicsFlag;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.stream.Stream;

public class SBlockPosition extends SPosition<Integer> implements BlockPosition {

    public SBlockPosition(Location<World> location) {
        super(location);
    }

    @Override
    public Vector3Int getPosition() {
        return new Vector3Int(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ());
    }

    @Override
    public SBlockPosition setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        org.spongepowered.api.block.BlockState state = ((SBlockDetails)details).getState();
        SApplyPhysicsFlag physicsFlag = (SApplyPhysicsFlag) Stream.of(flags).filter(f -> f instanceof ApplyPhysicsFlag).findAny().orElse(ApplyPhysicsFlags.NONE);
        this.location.setBlock(state, physicsFlag.getFlag());
        if (details.get(KeyedData.TILED_ENTITY).isPresent()){
            TileEntitySnapshot<? extends TileEntity> snapshot = details.get(KeyedData.TILED_ENTITY).get();
            try {
                snapshot.apply(this);
            } catch (BlockNotSupported blockNotSupported) {
                blockNotSupported.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public Position<Integer> setBlock(BlockDetails details, LivePlayer... players) {
        for(LivePlayer player : players){
            if(player.getPosition().getWorld().equals(this.getWorld())) {
                ((SLivePlayer) player).getSpongeEntity().sendBlockChange(this.location.getBlockPosition(), ((SBlockDetails)details).getState());
            }
        }
        return this;
    }

    @Override
    public Position<Integer> resetBlock(LivePlayer... players) {
        for(LivePlayer player : players){
            if(player.getPosition().getWorld().equals(this.getWorld())) {
                ((SLivePlayer) player).getSpongeEntity().resetBlockChange(this.location.getBlockPosition());
            }
        }
        return this;
    }
}
