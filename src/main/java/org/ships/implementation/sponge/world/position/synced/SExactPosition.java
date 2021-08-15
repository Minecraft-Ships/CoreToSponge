package org.ships.implementation.sponge.world.position.synced;

import org.core.entity.living.human.player.LivePlayer;
import org.core.vector.type.Vector3;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.position.impl.sync.SyncPosition;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SExactPosition extends SSyncedPosition<Double> implements SyncExactPosition {

    public SExactPosition(Location<? extends World<?, ?>, ?> location) {
        super(location, SExactPosition.TO_SYNCED_EXACT_POSITION);
    }

    @Override
    public SExactPosition getRelative(Vector3<?> vector) {
        return new SExactPosition(this.location.add(vector.getRawX().doubleValue(), vector.getRawY().doubleValue(), vector.getRawZ().doubleValue()));
    }

    @Override
    public SExactPosition getRelative(Direction direction) {
        return getRelative(direction.getAsVector());
    }

    @Override
    public BlockSnapshot.SyncBlockSnapshot getBlockDetails() {
        return toBlockPosition().getBlockDetails();
    }

    @Override
    public Vector3<Double> getPosition() {
        return Vector3.valueOf(this.location.x(), this.location.y(), this.location.z());
    }

    @Override
    public SyncBlockPosition toBlockPosition() {
        return new SBlockPosition(this.location);
    }

    @Override
    public SExactPosition setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        toBlockPosition().setBlock(details, flags);
        return this;
    }

    @Override
    public SyncPosition<Double> setBlock(BlockDetails details, LivePlayer... player) {
        toBlockPosition().setBlock(details, player);
        return this;
    }

    @Override
    public SExactPosition resetBlock(LivePlayer... player) {
        toBlockPosition().resetBlock(player);
        return this;
    }
}
