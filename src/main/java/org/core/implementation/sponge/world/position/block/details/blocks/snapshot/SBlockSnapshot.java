package org.core.implementation.sponge.world.position.block.details.blocks.snapshot;

import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.implementation.sponge.world.position.SPosition;
import org.core.implementation.sponge.world.position.block.SBlockType;
import org.core.implementation.sponge.world.position.block.details.blocks.StateDetails;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.*;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class SBlockSnapshot<P extends BlockPosition> implements BlockSnapshot<P>, StateDetails {

    public static class SAsyncedBlockSnapshot extends SBlockSnapshot<ASyncBlockPosition> implements BlockSnapshot.AsyncBlockSnapshot {

        public SAsyncedBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot) {
            super(snapshot);
        }

        public SAsyncedBlockSnapshot(ServerLocation location, Function<Location<? extends World<?, ?>, ?>, ASyncBlockPosition> newInstance) {
            super(location, newInstance);
        }

        public SAsyncedBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot, TileEntitySnapshot<? extends LiveTileEntity> tileEntity) {
            super(snapshot, tileEntity);
        }

        @Override
        protected SBlockSnapshot<ASyncBlockPosition> createCopyOf(org.spongepowered.api.block.BlockSnapshot snapshot, TileEntitySnapshot<? extends LiveTileEntity> tileEntity) {
            return new SAsyncedBlockSnapshot(snapshot, tileEntity);
        }

        @Override
        public AsyncBlockSnapshot createCopyOf() {
            return new SAsyncedBlockSnapshot(this.snapshot.copy(), this.tileEntitySnapshot.getSnapshot());
        }
    }

    public static class SSyncedBlockSnapshot extends SBlockSnapshot<SyncBlockPosition> implements BlockSnapshot.SyncBlockSnapshot {

        public SSyncedBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot) {
            super(snapshot);
        }

        public SSyncedBlockSnapshot(ServerLocation location, Function<Location<? extends World<?, ?>, ?>, SyncBlockPosition> newInstance) {
            super(location, newInstance);
        }

        public SSyncedBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot, TileEntitySnapshot<? extends LiveTileEntity> tileEntity) {
            super(snapshot, tileEntity);
        }

        @Override
        protected SSyncedBlockSnapshot createCopyOf(org.spongepowered.api.block.BlockSnapshot snapshot, TileEntitySnapshot<? extends LiveTileEntity> tileEntity) {
            return new SSyncedBlockSnapshot(snapshot, tileEntity);
        }

        @Override
        public AsyncBlockSnapshot asAsynced() {
            return this.createSnapshot(Position.toASync(this.getPosition()));
        }

        @Override
        public BlockSnapshot.SyncBlockSnapshot createCopyOf() {
            return new SSyncedBlockSnapshot(this.snapshot.copy(), this.tileEntitySnapshot.getSnapshot());
        }
    }

    public class STileEntityKeyedData implements TileEntityKeyedData {

        @Override
        public Optional<TileEntitySnapshot<? extends TileEntity>> getData() {
            return Optional.ofNullable(SBlockSnapshot.this.tileEntitySnapshot);
        }

        @Override
        public void setData(TileEntitySnapshot<? extends TileEntity> value) {
            SBlockSnapshot.this.tileEntitySnapshot = value;
        }
    }

    protected TileEntitySnapshot<? extends TileEntity> tileEntitySnapshot;
    protected org.spongepowered.api.block.BlockSnapshot snapshot;
    protected Function<Location<? extends World<?, ?>, ?>, P> newPosition;

    public SBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot) {
        this.snapshot = snapshot;
        this.newPosition = (Function<Location<? extends World<?, ?>, ?>, P>) SPosition.TO_SYNCED_BLOCK_POSITION;
        if (this.tileEntitySnapshot!=null && this.tileEntitySnapshot instanceof SignTileEntitySnapshot) {
            Optional<List<Component>> opLines = this.snapshot.get(Keys.SIGN_LINES);
            if (opLines.isPresent()) {
                List<Component> lines = opLines.get();
                for (int A = 0; A < lines.size(); A++) {
                    SignTileEntity sign = (SignTileEntity) this.tileEntitySnapshot;
                    sign.setTextAt(A, new AdventureText(lines.get(A)));
                }
            }
        }
    }

    protected abstract SBlockSnapshot<P> createCopyOf(org.spongepowered.api.block.BlockSnapshot snapshot, TileEntitySnapshot<? extends LiveTileEntity> tileEntity);

    public SBlockSnapshot(ServerLocation location, Function<Location<? extends World<?, ?>, ?>, P> newInstance) {
        this.snapshot = location.world().createSnapshot(location.blockPosition());
        this.newPosition = newInstance;
        Optional<? extends org.spongepowered.api.block.entity.BlockEntity> opTileEntity = location.blockEntity();
        opTileEntity.flatMap(blockEntity -> ((SpongePlatform) TranslateCore.getPlatform()).createTileEntityInstance(blockEntity)).ifPresent(te -> this.tileEntitySnapshot = te.getSnapshot());
    }

    public SBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot, TileEntitySnapshot<? extends LiveTileEntity> tileEntity) {
        this.snapshot = snapshot;
        this.newPosition = (Function<org.spongepowered.api.world.Location<? extends World<?, ?>, ?>, P>) SPosition.TO_SYNCED_BLOCK_POSITION;
        this.tileEntitySnapshot = tileEntity;
    }

    public org.spongepowered.api.block.BlockSnapshot getSnapshot() {
        return this.snapshot;
    }

    @Override
    public AsyncBlockSnapshot createSnapshot(ASyncBlockPosition position) {
        SBlockPosition position1 = (SBlockPosition) position;
        org.spongepowered.api.block.BlockSnapshot snapshot;
        if (position1.getSpongeLocation().world() instanceof ServerWorld) {
            snapshot = org.spongepowered.api.block.BlockSnapshot.builder()
                    .from(this.snapshot)
                    .world(((ServerWorld) position1.getSpongeLocation().world()).properties())
                    .position(position1.getSpongeLocation().blockPosition()).build();
        } else {
            snapshot = org.spongepowered.api.block.BlockSnapshot.builder()
                    .from(this.snapshot)
                    .position(position1.getSpongeLocation().blockPosition())
                    .build();
        }
        if (this.tileEntitySnapshot!=null && this.tileEntitySnapshot instanceof SignTileEntitySnapshot) {
            List<Component> lines = new ArrayList<>();
            for (AText text : ((SignTileEntity) this.tileEntitySnapshot).getText()) {
                AdventureText aText = (AdventureText) text;
                lines.add(aText.getComponent());
            }
            snapshot = snapshot.with(Keys.SIGN_LINES, lines).orElseThrow(() -> new IllegalStateException("Cannot get " +
                    "blocksnapshot with sign lines"));
        }
        return new SAsyncedBlockSnapshot(snapshot, this.tileEntitySnapshot);
    }

    @Override
    public SyncBlockSnapshot createSnapshot(SyncBlockPosition position) {
        SBlockPosition position1 = (SBlockPosition) position;
        org.spongepowered.api.block.BlockSnapshot snapshot;
        if (position1.getSpongeLocation().world() instanceof ServerWorld) {
            snapshot = org.spongepowered.api.block.BlockSnapshot.builder()
                    .from(this.snapshot)
                    .world(((ServerWorld) position1.getSpongeLocation().world()).properties())
                    .position(position1.getSpongeLocation().blockPosition()).build();
        } else {
            snapshot = org.spongepowered.api.block.BlockSnapshot.builder()
                    .from(this.snapshot)
                    .position(position1.getSpongeLocation().blockPosition())
                    .build();
        }
        if (this.tileEntitySnapshot!=null && this.tileEntitySnapshot instanceof SignTileEntitySnapshot) {
            List<Component> lines = new ArrayList<>();
            for (AText text : ((SignTileEntity) this.tileEntitySnapshot).getText()) {
                lines.add(((AdventureText) text).getComponent());
            }
            snapshot = snapshot.with(Keys.SIGN_LINES, lines).orElseThrow(() -> new IllegalStateException("Cannot get " +
                    "blocksnapshot with sign lines"));
        }
        return new SSyncedBlockSnapshot(snapshot, this.tileEntitySnapshot);
    }

    @Override
    public org.spongepowered.api.block.BlockState getState() {
        return this.getSnapshot().state();
    }

    private <T> Optional<KeyedData<T>> getKey(Class<? extends KeyedData<T>> data) {
        KeyedData<T> key = null;
        if (data.isAssignableFrom(TileEntityKeyedData.class)) {
            key = (KeyedData<T>) new SBlockSnapshot<?>.STileEntityKeyedData();
        } else if (data.isAssignableFrom(OpenableKeyedData.class) && (this.snapshot.supports(Keys.IS_OPEN))) {
            //TODO
        } else if (data.isAssignableFrom(AttachableKeyedData.class) && this.snapshot.supports(Keys.IS_ATTACHED)) {
            //TODO
        } else if (data.isAssignableFrom(MultiDirectionalKeyedData.class) && this.snapshot.supports(Keys.CONNECTED_DIRECTIONS)) {
            //TODO
        }
        return Optional.ofNullable(key);
    }

    public <T> boolean setKey(Key<? extends Value<T>> key, T value) {
        Optional<org.spongepowered.api.block.BlockSnapshot> opState = this.snapshot.with(key, value);
        if (opState.isPresent()) {
            this.snapshot = opState.get();
            return true;
        }
        return false;
    }

    @Override
    public BlockType getType() {
        return new SBlockType(this.snapshot.state().type());
    }

    @Override
    @Deprecated
    public <BP extends BlockPosition> BlockSnapshot<BP> createSnapshot(BP position) {
        if (position instanceof SyncBlockPosition) {
            return (BlockSnapshot<BP>) this.createSnapshot((SyncBlockPosition) position);
        }
        return (BlockSnapshot<BP>) this.createSnapshot((ASyncBlockPosition) position);
    }

    @Override
    public Optional<DirectionalData> getDirectionalData() {
        if (this.snapshot.supports(Keys.DIRECTION)) {
            return Optional.of(new DirectionSnapshotWrapper(this));
        }
        return Optional.empty();
    }

    @Override
    public P getPosition() {
        return this.newPosition.apply(this.snapshot.location().orElseThrow(() -> new IllegalStateException("Cannot " +
                "get location from snapshot")));
    }

    @Override
    public <T> Optional<T> get(Class<? extends KeyedData<T>> data) {
        Optional<KeyedData<T>> opKey = this.getKey(data);
        if (opKey.isPresent()) {
            return opKey.get().getData();
        }
        return Optional.empty();
    }

    @Override
    public <T> BlockDetails set(Class<? extends KeyedData<T>> data, T value) {
        Optional<KeyedData<T>> opKey = this.getKey(data);
        opKey.ifPresent(k -> k.setData(value));
        return this;
    }

}
