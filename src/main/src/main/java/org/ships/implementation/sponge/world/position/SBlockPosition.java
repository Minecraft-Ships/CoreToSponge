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
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.world.position.block.details.blocks.SBlockDetail;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SBlockPosition extends SPosition<Integer> implements BlockPosition {

    public SBlockPosition(Location<World> location) {
        super(location);
    }

    @Override
    public Vector3Int getPosition() {
        return new Vector3Int(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ());
    }

    @Override
    public SBlockPosition setBlock(BlockDetails details) {
        org.spongepowered.api.block.BlockState state = ((SBlockDetail)details).getState();
        this.location.setBlock(state, BlockChangeFlags.NONE);
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
                ((SLivePlayer) player).getSpongeEntity().sendBlockChange(this.location.getBlockPosition(), ((SBlockDetail)details).getState());
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
