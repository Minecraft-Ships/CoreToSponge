package org.core.implementation.sponge.utils;

import org.core.world.direction.Direction;
import org.core.world.direction.FourFacingDirection;
import org.core.world.direction.SixteenFacingDirection;

import java.util.stream.Stream;

public interface DirectionUtils {

    static org.spongepowered.api.util.Direction getSpongeDirection(Direction direction) {
        if (direction.equals(FourFacingDirection.NONE)) {
            return org.spongepowered.api.util.Direction.NONE;
        }
        var directions = org.spongepowered.api.util.Direction.values();
        return Stream
                .of(directions)
                .filter(dir -> {
                    var spongeDirectionName = dir.name().replaceAll("_", "");
                    var coreDirectionName = direction.getName().replaceAll("_", "");
                    return spongeDirectionName.equalsIgnoreCase(coreDirectionName);
                })
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot find a Sponge direction for " + direction.getName() + " (" + direction
                                .getName()
                                .replaceAll("_", "") + ")"));
    }

    static Direction getCoreDirection(org.spongepowered.api.util.Direction direction) {
        if (direction == org.spongepowered.api.util.Direction.NONE) {
            return FourFacingDirection.NONE;
        }
        var directions = Direction.withYDirections(SixteenFacingDirection.getSixteenFacingDirections());
        return Stream
                .of(directions)
                .filter(dir -> {
                    var coreDirectionName = dir.getName().replaceAll("_", "");
                    var spongeDirectionName = direction.name().replaceAll("_", "");
                    return coreDirectionName.equalsIgnoreCase(spongeDirectionName);
                })
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot find a TranslateCore direction for " + direction.name() + " (" + direction
                                .name()
                                .replaceAll("_", "") + ")"));
    }
}
