package org.ships.implementation.sponge.world.position.block.details.blocks.snapshot;

import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.impl.BlockPosition;

import java.util.Optional;

public class SFakeBlockSnapshot<P extends BlockPosition> implements BlockSnapshot<P> {

    private BlockDetails details;
    private P position;

    public SFakeBlockSnapshot(P position, BlockDetails details) {
        this.position = position;
        this.details = details;
    }

    @Override
    public P getPosition() {
        return this.position;
    }

    @Override
    public BlockType getType() {
        return this.details.getType();
    }

    @Override
    public <T extends BlockPosition> BlockSnapshot<T> createSnapshot(T position) {
        return new SFakeBlockSnapshot<T>(position, this.details.createCopyOf());
    }

    @Override
    public Optional<DirectionalData> getDirectionalData() {
        return this.details.getDirectionalData();
    }

    @Override
    public <T> Optional<T> get(Class<? extends KeyedData<T>> data) {
        return this.details.get(data);
    }

    @Override
    public <T> BlockDetails set(Class<? extends KeyedData<T>> data, T value) {
        return this.details.set(data, value);
    }

    @Override
    public BlockSnapshot<P> createCopyOf() {
        return new SFakeBlockSnapshot<>(this.position, this.details.createCopyOf());
    }
}
