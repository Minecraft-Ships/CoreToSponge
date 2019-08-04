package org.ships.implementation.sponge.world.position.block.details.blocks;

import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.ships.implementation.sponge.world.position.block.SBlockType;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;

import java.util.Optional;

public class SBlockDetails implements BlockDetails, SBlockDetail {

    protected org.spongepowered.api.block.BlockState blockstate;

    public SBlockDetails(org.spongepowered.api.block.BlockState state){
        this.blockstate = state;
    }

    @Override
    public BlockType getType() {
        return new SBlockType(this.blockstate.getType());
    }

    @Override
    public Optional<DirectionalData> getDirectionalData() {
        if(this.blockstate.supports(Keys.DIRECTION)){
            return Optional.of(new DirectionWrapper(this));
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> get(Class<? extends KeyedData<T>> data) {
        return Optional.empty();
    }

    @Override
    public <T> BlockDetails set(Class<? extends KeyedData<T>> data, T value) {
        return null;
    }

    @Override
    public BlockDetails createCopyOf() {
        return new SBlockDetails(this.blockstate.copy());
    }

    @Override
    public BlockState getState() {
        return this.blockstate;
    }

    @Override
    public <T> boolean setKey(Key key, T value) {
        Optional<BlockState> opState = this.blockstate.with(key, value);
        if(opState.isPresent()){
            this.blockstate = opState.get();
            return true;
        }
        return false;
    }
}
