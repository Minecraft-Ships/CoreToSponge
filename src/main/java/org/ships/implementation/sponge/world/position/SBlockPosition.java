package org.ships.implementation.sponge.world.position;

import org.core.vector.types.Vector3Int;
import org.core.world.position.BlockPosition;
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
}
