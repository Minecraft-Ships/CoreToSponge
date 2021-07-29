package org.ships.implementation.sponge.world.position.asynced;

import org.core.vector.type.Vector3;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.ships.implementation.sponge.world.position.synced.SExactPosition;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SAsyncedExactPosition extends SAsyncedPosition<Double> implements ASyncExactPosition {

    public SAsyncedExactPosition(Location<? extends World<?, ?>, ?> location) {
        super(location, SAsyncedExactPosition.TO_ASYNCED_EXACT_POSITION);
    }

    @Override
    public BlockSnapshot<? extends BlockPosition> getBlockDetails() {
        return this.toBlockPosition().getBlockDetails();
    }

    @Override
    public Vector3<Double> getPosition() {
        return Vector3.valueOf(this.location.x(), this.location.y(), this.location.z());
    }

    @Override
    public ASyncBlockPosition toBlockPosition() {
        return new SAsyncedBlockPosition(this.location);
    }

    @Override
    protected SExactPosition toSynced() {
        return new SExactPosition(this.location);
    }
}
