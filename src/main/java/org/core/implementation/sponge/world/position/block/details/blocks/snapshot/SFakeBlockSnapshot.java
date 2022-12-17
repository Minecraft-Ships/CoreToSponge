package org.core.implementation.sponge.world.position.block.details.blocks.snapshot;

import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Optional;

public abstract class SFakeBlockSnapshot<P extends BlockPosition> implements BlockSnapshot<P> {

    protected final BlockDetails details;
    protected final P position;

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

    public static class SFakeAsyncedBlockSnapshot extends SFakeBlockSnapshot<ASyncBlockPosition>
            implements BlockSnapshot.AsyncBlockSnapshot {

        public SFakeAsyncedBlockSnapshot(ASyncBlockPosition position, BlockDetails details) {
            super(position, details);
        }

        @Override
        public AsyncBlockSnapshot createSnapshot(ASyncBlockPosition position) {
            return new SFakeAsyncedBlockSnapshot(position, this.details.createCopyOf());
        }

        @Override
        public SyncBlockSnapshot createSnapshot(SyncBlockPosition position) {
            return new SFakeSyncedBlockSnapshot(position, this.details.createCopyOf());
        }

        @Override
        public AsyncBlockSnapshot createCopyOf() {
            return this.createSnapshot(this.position);
        }
    }

    public static class SFakeSyncedBlockSnapshot extends SFakeBlockSnapshot<SyncBlockPosition>
            implements BlockSnapshot.SyncBlockSnapshot {

        public SFakeSyncedBlockSnapshot(SyncBlockPosition position, BlockDetails details) {
            super(position, details);
        }

        @Override
        public AsyncBlockSnapshot createSnapshot(ASyncBlockPosition position) {
            return new SFakeAsyncedBlockSnapshot(position, this.details.createCopyOf());
        }

        @Override
        public SyncBlockSnapshot createSnapshot(SyncBlockPosition position) {
            return new SFakeSyncedBlockSnapshot(position, this.details.createCopyOf());
        }

        @Override
        public AsyncBlockSnapshot asAsynced() {
            return this.createSnapshot(Position.toASync(this.position));
        }

        @Override
        public SyncBlockSnapshot createCopyOf() {
            return this.createSnapshot(this.position);
        }
    }
}
