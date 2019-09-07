package org.ships.implementation.sponge.world.position.block.details.blocks;

import org.core.world.position.BlockPosition;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.ships.implementation.sponge.world.position.SBlockPosition;
import org.ships.implementation.sponge.world.position.block.SBlockType;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;

import java.util.Optional;

public class SBlockSnapshot implements BlockSnapshot, SBlockDetail {

    protected org.spongepowered.api.block.BlockSnapshot snapshot;

    public SBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot){
        this.snapshot = snapshot;
    }

    @Override
    public BlockType getType() {
        return new SBlockType(this.snapshot.getState().getType());
    }

    @Override
    public BlockSnapshot createSnapshot(BlockPosition position) {
        SBlockPosition position1 = (SBlockPosition) position;
        org.spongepowered.api.block.BlockSnapshot snapshot = org.spongepowered.api.block.BlockSnapshot.builder().from(this.snapshot).world(position1.getSpongeLocation().getExtent().getProperties()).position(position1.getSpongeLocation().getBlockPosition()).build();
        return new SBlockSnapshot(snapshot);
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
    public BlockPosition getPosition() {
        return new SBlockPosition(this.snapshot.getLocation().get());
    }

    @Override
    public BlockSnapshot createCopyOf() {
        return new SBlockSnapshot(this.snapshot.copy());
    }

    @Override
    public BlockState getState() {
        return this.snapshot.getState();
    }

    @Override
    public <T> boolean setKey(Key key, T value) {
        Optional<org.spongepowered.api.block.BlockSnapshot> opSnapshot = this.snapshot.with(key, value);
        if(opSnapshot.isPresent()){
            this.snapshot = opSnapshot.get();
            return true;
        }
        return false;
    }
}
