package org.ships.implementation.sponge.world.position.block.details.blocks;

import org.core.world.position.block.details.BlockDetails;
import org.spongepowered.api.block.BlockState;

public class SGeneralBlockDetails extends AbstractBlockDetails implements BlockDetails {

    public SGeneralBlockDetails(BlockState state) {
        super(state);
    }

    @Override
    public BlockDetails createCopyOf() {
        return new SGeneralBlockDetails(this.blockstate.copy());
    }
}
