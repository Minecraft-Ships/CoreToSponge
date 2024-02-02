package org.core.implementation.sponge.world.position.block.details.blocks.snapshot;

import org.core.exceptions.DirectionNotSupported;
import org.core.implementation.sponge.utils.DirectionUtils;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.impl.Position;
import org.spongepowered.api.data.Keys;

import java.util.HashSet;
import java.util.Set;

public class DirectionSnapshotWrapper implements DirectionalData {

    private final SBlockSnapshot<? extends Position<Integer>> details;

    public DirectionSnapshotWrapper(SBlockSnapshot<? extends Position<Integer>> details) {
        this.details = details;
    }

    @Override
    public Direction getDirection() {
        return DirectionUtils.getCoreDirection(this.details.getState().get(Keys.DIRECTION).orElseThrow());
    }

    @Override
    public DirectionalData setDirection(Direction direction) throws DirectionNotSupported {
        org.spongepowered.api.util.Direction direction1 = DirectionUtils.getSpongeDirection(direction);
        boolean sameDirection = this.details
                .getState()
                .get(Keys.DIRECTION)
                .map(current -> current == direction1)
                .orElse(false);
        if (sameDirection) {
            return this;
        }
        boolean check = this.details.setKey(Keys.DIRECTION, direction1);
        if (!check) {
            new DirectionNotSupported(direction, this.details.getState().asString()).printStackTrace();
        }
        return this;
    }

    @Override
    public Direction[] getSupportedDirections() {
        Set<Direction> set = new HashSet<>();
        for (org.spongepowered.api.util.Direction direction : org.spongepowered.api.util.Direction.values()) {
            if (this.details.getState().copy().with(Keys.DIRECTION, direction).isPresent()) {
                set.add(DirectionUtils.getCoreDirection(direction));
            }
        }
        return set.toArray(new Direction[0]);
    }
}
