package org.ships.implementation.sponge.world.position.block.details.blocks.details;

import org.core.exceptions.DirectionNotSupported;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.data.DirectionalData;
import org.ships.implementation.sponge.utils.DirectionUtils;
import org.spongepowered.api.data.key.Keys;

import java.util.HashSet;
import java.util.Set;

public class DirectionStateWrapper implements DirectionalData {

    private SBlockDetails details;

    public DirectionStateWrapper(SBlockDetails details){
        this.details = details;
    }

    @Override
    public Direction getDirection() {
        return DirectionUtils.getCoreDirection(this.details.getState().get(Keys.DIRECTION).get());
    }

    @Override
    public Direction[] getSupportedDirections() {
        Set<Direction> set = new HashSet<>();
        for(org.spongepowered.api.util.Direction direction : org.spongepowered.api.util.Direction.values()){
            if (this.details.getState().copy().with(Keys.DIRECTION, direction).isPresent()){
                set.add(DirectionUtils.getCoreDirection(direction));
            }
        }
        return set.toArray(new Direction[0]);
    }

    @Override
    public DirectionalData setDirection(Direction direction) throws DirectionNotSupported {
        org.spongepowered.api.util.Direction direction1 = DirectionUtils.getSpongeDirection(direction);
        System.out.println("BlockState: " + this.details.getClass().getSimpleName());
        boolean check = this.details.setKey(Keys.DIRECTION, direction1);
        if (!check){
            System.err.println("Direction not supported");
            throw new DirectionNotSupported(direction, this.details.getState().getType().getId());
        }
        return this;
    }
}