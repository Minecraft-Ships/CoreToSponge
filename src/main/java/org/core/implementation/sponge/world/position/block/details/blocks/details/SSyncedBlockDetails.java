package org.core.implementation.sponge.world.position.block.details.blocks.details;

import org.core.TranslateCore;
import org.core.implementation.sponge.world.position.SPosition;
import org.core.implementation.sponge.world.position.block.details.blocks.snapshot.SBlockSnapshot;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.details.data.keyed.TileEntityKeyedData;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Optional;

public class SSyncedBlockDetails extends SBlockDetails {

    protected TileEntitySnapshot<? extends TileEntity> tileEntitySnapshot;

    public SSyncedBlockDetails(org.spongepowered.api.block.BlockState state) {
        super(state);
        TranslateCore.getPlatform().getDefaultTileEntity(this.getType()).ifPresent(t -> this.tileEntitySnapshot = t);
    }

    public SSyncedBlockDetails(SBlockPosition position) {
        super(position.getSpongeLocation().block());
        if (position.getTileEntity().isPresent()) {
            this.tileEntitySnapshot = position.getTileEntity().get().getSnapshot();
        }
    }

    @Override
    public BlockSnapshot.AsyncBlockSnapshot createSnapshot(ASyncBlockPosition position) {
        Location<? extends World<?, ?>, ?> location = (((SPosition<Integer>) position).getSpongeLocation());
        return new SBlockSnapshot.SAsyncedBlockSnapshot(org.spongepowered.api.block.BlockSnapshot
                .builder()
                .blockState(this.blockstate)
                .world(((ServerWorld) location.world()).properties())
                .position(location.blockPosition())
                .build());
    }

    @Override
    public BlockSnapshot.SyncBlockSnapshot createSnapshot(SyncBlockPosition position) {
        Location<? extends World<?, ?>, ?> location = (((SPosition<Integer>) position).getSpongeLocation());
        return new SBlockSnapshot.SSyncedBlockSnapshot(org.spongepowered.api.block.BlockSnapshot
                .builder()
                .blockState(this.blockstate)
                .world(((ServerWorld) location.world()).properties())
                .position(location.blockPosition())
                .build());
    }

    @Override
    public BlockDetails createCopyOf() {
        return new SSyncedBlockDetails(this.blockstate.copy());
    }

    @Override
    protected <T> Optional<KeyedData<T>> getKey(Class<? extends KeyedData<T>> data) {
        if (data.isAssignableFrom(TileEntityKeyedData.class)) {
            return Optional.of((KeyedData<T>) new STileEntityKeyedData());
        }
        return super.getKey(data);
    }

    public class STileEntityKeyedData implements TileEntityKeyedData {

        @Override
        public Optional<TileEntitySnapshot<? extends TileEntity>> getData() {
            return Optional.ofNullable(SSyncedBlockDetails.this.tileEntitySnapshot);
        }

        @Override
        public void setData(TileEntitySnapshot<? extends TileEntity> value) {
            SSyncedBlockDetails.this.tileEntitySnapshot = value;
        }
    }
}
