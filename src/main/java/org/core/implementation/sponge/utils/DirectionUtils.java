package org.core.implementation.sponge.utils;

import org.core.vector.type.Vector3;
import org.core.world.direction.Direction;
import org.core.world.direction.SixteenFacingDirection;
import org.spongepowered.math.vector.Vector3i;

import java.util.stream.Stream;

public interface DirectionUtils {

    static org.spongepowered.api.util.Direction getSpongeDirection(Direction direction) {
        Vector3<Integer> vector = direction.getAsVector();
        return Stream
                .of(org.spongepowered.api.util.Direction.values())
                .filter(d -> compare(vector, d.asBlockOffset()))
                .findFirst()
                .get();
    }

    static Direction getCoreDirection(org.spongepowered.api.util.Direction direction) {
        return Stream
                .of(Direction.withYDirections(SixteenFacingDirection.getSixteenFacingDirections()))
                .filter(d -> compare(d.getAsVector(), direction.asBlockOffset()))
                .findFirst()
                .get();
    }

    static boolean compare(Vector3<Integer> vector, Vector3i vector2) {
        return (vector.getZ() == vector2.z()) && (vector.getY() == vector2.y()) && (vector.getX() == vector2.x());
    }
}
