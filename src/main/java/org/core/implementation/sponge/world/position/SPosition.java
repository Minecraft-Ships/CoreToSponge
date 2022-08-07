package org.core.implementation.sponge.world.position;

import org.core.implementation.sponge.world.SWorldExtent;
import org.core.implementation.sponge.world.position.asynced.SAsyncedBlockPosition;
import org.core.implementation.sponge.world.position.asynced.SAsyncedExactPosition;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.implementation.sponge.world.position.synced.SExactPosition;
import org.core.vector.type.Vector3;
import org.core.world.WorldExtent;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncPosition;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.math.vector.Vector3i;

import java.util.function.Function;

public abstract class SPosition<N extends Number> implements Position<N> {

    protected final Location<? extends World<?, ?>, ?> location;    public static final Function<? extends Location<? extends World<?, ?>, ?>, SBlockPosition> TO_SYNCED_BLOCK_POSITION = (SBlockPosition::new);
    protected final Function<? extends Location<? extends World<?, ?>, ?>, ? extends SPosition<N>> newInstance;    public static final Function<? extends Location<? extends World<?, ?>, ?>, SExactPosition> TO_SYNCED_EXACT_POSITION = (SExactPosition::new);
    public SPosition(Location<? extends World<?, ?>, ?> location,
            Function<? extends Location<? extends World<?, ?>, ?>, ? extends SPosition<N>> newInstance) {
        this.location = location;
        this.newInstance = newInstance;
    }    public static final Function<? extends Location<? extends World<?, ?>, ?>, SAsyncedBlockPosition> TO_ASYNCED_BLOCK_POSITION = (SAsyncedBlockPosition::new);

    public Location<? extends World<?, ?>, ?> getSpongeLocation() {
        return this.location;
    }    public static final Function<? extends Location<? extends World<?, ?>, ?>, SAsyncedExactPosition> TO_ASYNCED_EXACT_POSITION = (SAsyncedExactPosition::new);

    @Override
    public Vector3<Integer> getChunkPosition() {
        Vector3i vector = this.location.chunkPosition();
        return Vector3.valueOf(vector.x(), vector.y(), vector.z());
    }

    @Override
    public WorldExtent getWorld() {
        return new SWorldExtent(this.location.world());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SyncPosition)) {
            return false;
        }
        Position<? extends Number> pos = (Position<? extends Number>) obj;
        if (!pos.getWorld().equals(this.getWorld())) {
            return false;
        }
        return pos.getPosition().equals(this.getPosition());
    }

    @Override
    public String toString() {
        return this.getX() + "," + this.getY() + "," + this.getZ() + "," + this.getWorld().getName();
    }








}
