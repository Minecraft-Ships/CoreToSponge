package org.ships.implementation.sponge.world.position;

import org.core.entity.living.human.player.LivePlayer;
import org.core.vector.Vector3;
import org.core.vector.types.Vector3Double;
import org.core.world.position.BlockPosition;
import org.core.world.position.ExactPosition;
import org.core.world.position.Position;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.flags.PositionFlag;
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
    public ExactPosition getRelative(Vector3<Double> vector) {
        return new SExactPosition(this.location.getExtent().getLocation(this.getX() + vector.getX(), this.getY() + vector.getY(), this.getZ() + vector.getZ()));
    }

    @Override
    public BlockPosition toBlockPosition() {
        return new SBlockPosition(this.location);
    }

    @Override
    public SExactPosition setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        toBlockPosition().setBlock(details, flags);
        return this;
    }

    @Override
    public Position<Double> setBlock(BlockDetails details, LivePlayer... player) {
        toBlockPosition().setBlock(details, player);
        return this;
    }

    @Override
    public Position<Double> resetBlock(LivePlayer... player) {
        toBlockPosition().resetBlock(player);
        return this;
    }
}
