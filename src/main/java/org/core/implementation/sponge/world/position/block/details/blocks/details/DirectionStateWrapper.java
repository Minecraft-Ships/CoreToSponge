package org.core.implementation.sponge.world.position.block.details.blocks.details;

import org.core.exceptions.DirectionNotSupported;
import org.core.implementation.sponge.utils.DirectionUtils;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.data.DirectionalData;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.stream.Stream;

public class DirectionStateWrapper implements DirectionalData {

    private final SBlockDetails details;

    public DirectionStateWrapper(SBlockDetails details) {
        this.details = details;
    }

    @Override
    public Direction getDirection() {
        org.spongepowered.api.util.Direction direction = this.details.getState().get(Keys.DIRECTION).orElseThrow();
        direction = direction.opposite();
        return DirectionUtils.getCoreDirection(direction);
    }

    @Override
    public DirectionalData setDirection(Direction direction) throws DirectionNotSupported {
        direction = direction.getOpposite();
        org.spongepowered.api.util.Direction direction1 = DirectionUtils.getSpongeDirection(direction);
        boolean check = this.details.setKey(Keys.DIRECTION, direction1);
        if (!check) {
            throw new DirectionNotSupported(direction,
                                            this.details.getState().type().key(RegistryTypes.BLOCK_TYPE).asString());
        }
        return this;
    }

    @Override
    public Direction[] getSupportedDirections() {
        return Stream
                .of(org.spongepowered.api.util.Direction.values())
                .filter(direction -> this.details.getState().copy().with(Keys.DIRECTION, direction).isPresent())
                .map(DirectionUtils::getCoreDirection)
                .toArray(Direction[]::new);
    }
}
