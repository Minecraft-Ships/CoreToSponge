package org.ships.implementation.sponge.world.position.block.details.blocks;

import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.ships.implementation.sponge.world.position.block.SBlockType;

public abstract class AbstractBlockDetails implements BlockDetails {

    protected org.spongepowered.api.block.BlockState blockstate;

    public AbstractBlockDetails(org.spongepowered.api.block.BlockState state){
        this.blockstate = state;
    }

    public org.spongepowered.api.block.BlockState getBlockstate(){
        return this.blockstate;
    }

    @Override
    public BlockType getType() {
        return new SBlockType(this.blockstate.getType());
    }
}
