package org.ships.implementation.sponge.world.position;

import org.core.vector.types.Vector3Double;
import org.core.world.position.BlockPosition;
import org.core.world.position.ExactPosition;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SExactPosition extends SPosition<Double> implements ExactPosition {

    public SExactPosition(Location<World> location) {
        super(location);
    }

    @Override
    public Vector3Double getPosition() {
        return new Vector3Double(this.location.getX(), this.location.getY(), this.location.getZ());
    }

    @Override
    public BlockPosition toBlockPosition() {
        return new SBlockPosition(this.location);
    }
}
