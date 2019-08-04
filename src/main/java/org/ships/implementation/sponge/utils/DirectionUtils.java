package org.ships.implementation.sponge.utils;

import com.flowpowered.math.vector.Vector3i;
import org.core.vector.types.Vector3Int;
import org.core.world.direction.Direction;
import org.core.world.direction.SixteenFacingDirection;

import java.util.stream.Stream;

public interface DirectionUtils {

    static org.spongepowered.api.util.Direction getSpongeDirection(Direction direction){
        Vector3Int vector = direction.getAsVector();
        return Stream.of(org.spongepowered.api.util.Direction.values()).filter(d -> compare(vector, d.asBlockOffset())).findFirst().get();
    }

    static Direction getCoreDirection(org.spongepowered.api.util.Direction direction){
        return Stream.of(SixteenFacingDirection.getSixteenFacingDirections()).filter(d -> compare(d.getAsVector(), direction.asBlockOffset())).findFirst().get();
    }

    static boolean compare(Vector3Int vector, Vector3i vector2){
        return (vector.getZ() == vector2.getZ()) && (vector.getY() == vector2.getY()) && (vector.getX() == vector2.getX());
    }
}
