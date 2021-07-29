package org.ships.implementation.sponge.world.position.block.details.blocks.details;

import org.core.world.position.block.details.BlockDetails;
import org.spongepowered.api.block.BlockState;

public class SAsyncedBlockDetails extends SBlockDetails {

    public SAsyncedBlockDetails(BlockState state) {
        super(state);
    }

    @Override
    public BlockDetails createCopyOf() {
        return new SAsyncedBlockDetails(this.blockstate.copy());
    }
}
