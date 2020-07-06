package org.ships.implementation.sponge.world.position;

import com.flowpowered.math.vector.Vector3i;
import org.core.CorePlugin;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.vector.types.Vector3Int;
import org.core.world.WorldExtent;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncPosition;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.entity.LiveTileEntity;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.SWorldExtent;
import org.ships.implementation.sponge.world.position.block.details.blocks.snapshot.SBlockSnapshot;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;
import org.ships.implementation.sponge.world.position.synced.SExactPosition;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.function.Function;

public abstract class SPosition <N extends Number> implements Position<N> {

    public static final Function<Location<World>, SBlockPosition> TO_SYNCED_BLOCK_POSITION = ((l) -> new SBlockPosition(l));
    public static final Function<Location<World>, SExactPosition> TO_SYNCED_EXACT_POSITION = ((l) -> new SExactPosition(l));


    protected Location<World> location;
    protected Function<Location<World>, SPosition<N>> newInstance;

    public SPosition(Location<World> location, Function<Location<World>, SPosition<N>> newInstance){
        this.location = location;
        this.newInstance = newInstance;
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
        return new SBlockSnapshot(this.location, this.newInstance);
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
