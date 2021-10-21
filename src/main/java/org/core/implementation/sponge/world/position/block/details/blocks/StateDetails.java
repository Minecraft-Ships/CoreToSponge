package org.core.implementation.sponge.world.position.block.details.blocks;

import org.core.world.position.block.details.BlockDetails;
import org.spongepowered.api.block.BlockState;

public interface StateDetails extends BlockDetails {

    BlockState getState();
}
