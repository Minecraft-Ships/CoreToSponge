package org.ships.implementation.sponge.world.position.block.details.blocks;

import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.ships.implementation.sponge.world.position.block.SBlockType;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;

import java.util.Optional;

public class SExactBlockDetails implements BlockDetails, SBlockDetail {

    protected org.spongepowered.api.block.BlockSnapshot snapshot;

    public SExactBlockDetails(org.spongepowered.api.block.BlockSnapshot snapshot){
        this.snapshot = snapshot;
    }

    @Override
    public BlockType getType() {
        return new SBlockType(this.snapshot.getState().getType());
    }

    @Override
    public Optional<DirectionalData> getDirectionalData() {
        if(this.snapshot.getState().supports(Keys.DIRECTION)){
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
        return new SExactBlockDetails(this.snapshot.copy());
    }

    @Override
    public BlockState getState() {
        return this.snapshot.getState();
    }

    @Override
    public <T> boolean setKey(Key key, T value) {
        Optional<BlockSnapshot> opSnapshot = this.snapshot.with(key, value);
        if(opSnapshot.isPresent()){
            this.snapshot = opSnapshot.get();
            return true;
        }
        return false;
    }
}
