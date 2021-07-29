package org.ships.implementation.sponge.world.position.asynced;

import org.core.vector.type.Vector3;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.ships.implementation.sponge.world.position.block.details.blocks.details.SAsyncedBlockDetails;
import org.ships.implementation.sponge.world.position.block.details.blocks.snapshot.SFakeBlockSnapshot;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SAsyncedBlockPosition extends SAsyncedPosition<Integer> implements ASyncBlockPosition {

    public SAsyncedBlockPosition(Location<? extends World<?, ?>, ?> location) {
        super(location, SAsyncedPosition.TO_ASYNCED_BLOCK_POSITION);
    }

    @Override
    public BlockSnapshot<? extends BlockPosition> getBlockDetails() {
        return new SFakeBlockSnapshot<>(this, new SAsyncedBlockDetails(this.location.block()));
    }

    @Override
    public Vector3<Integer> getPosition() {
        return Vector3.valueOf(this.location.blockX(), this.location.blockY(), this.location.blockZ());
    }

    @Override
    public ASyncExactPosition toExactPosition() {
        return new SAsyncedExactPosition(this.location);
    }

    @Override
    protected SBlockPosition toSynced() {
        return new SBlockPosition(this.location);
    }
}
