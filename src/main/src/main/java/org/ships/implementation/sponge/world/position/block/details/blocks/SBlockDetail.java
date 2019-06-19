package org.ships.implementation.sponge.world.position.block.details.blocks;

import org.spongepowered.api.data.key.Key;

public interface SBlockDetail {

    org.spongepowered.api.block.BlockState getState();
    <T extends Object> boolean setKey(Key key, T value);
}
