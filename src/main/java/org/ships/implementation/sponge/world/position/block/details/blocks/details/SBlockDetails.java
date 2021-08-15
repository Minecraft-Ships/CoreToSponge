package org.ships.implementation.sponge.world.position.block.details.blocks.details;

import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.AttachableKeyedData;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.details.data.keyed.MultiDirectionalKeyedData;
import org.core.world.position.block.details.data.keyed.OpenableKeyedData;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.ships.implementation.sponge.world.position.block.SBlockType;
import org.ships.implementation.sponge.world.position.block.details.blocks.StateDetails;
import org.ships.implementation.sponge.world.position.block.details.blocks.snapshot.SBlockSnapshot;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Optional;

public abstract class SBlockDetails implements BlockDetails, StateDetails {

    protected org.spongepowered.api.block.BlockState blockstate;

    public SBlockDetails(org.spongepowered.api.block.BlockState state) {
        this.blockstate = state;
    }

    @Override
    public BlockType getType() {
        return new SBlockType(this.blockstate.type());
    }

    @Override
    @Deprecated
    public <P extends BlockPosition> BlockSnapshot<P> createSnapshot(P position) {
        /*org.spongepowered.api.world.Location<? extends World<?, ?>, ?> loc = ((SBlockPosition) position).getSpongeLocation();
        if (loc.world() instanceof ServerWorld) {
            org.spongepowered.api.block.BlockSnapshot blockSnapshot = org.spongepowered.api.block.BlockSnapshot.builder()
                    .blockState(this.blockstate)
                    .world(((ServerWorld) loc.world()).properties())
                    .position(loc.blockPosition())
                    .build();
            return new SBlockSnapshot<>(blockSnapshot);
        }
        org.spongepowered.api.block.BlockSnapshot blockSnapshot = org.spongepowered.api.block.BlockSnapshot.builder()
                .blockState(this.blockstate)
                .position(loc.blockPosition())
                .build();
        return new SBlockSnapshot<>(blockSnapshot);*/
        if(position instanceof SyncBlockPosition){
            return (BlockSnapshot<P>) createSnapshot((SyncBlockPosition) position);
        }
        return (BlockSnapshot<P>) createSnapshot((ASyncBlockPosition) position);
    }

    @Override
    public Optional<DirectionalData> getDirectionalData() {
        if (this.blockstate.supports(Keys.DIRECTION)) {
            return Optional.of(new DirectionStateWrapper(this));
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> get(Class<? extends KeyedData<T>> data) {
        Optional<KeyedData<T>> opKey = getKey(data);
        if (opKey.isPresent()) {
            return opKey.get().getData();
        }
        return Optional.empty();
    }

    @Override
    public <T> BlockDetails set(Class<? extends KeyedData<T>> data, T value) {
        Optional<KeyedData<T>> opKey = getKey(data);
        opKey.ifPresent(k -> k.setData(value));
        return this;
    }

    public BlockState getState() {
        return this.blockstate;
    }

    public <T> boolean setKey(Key key, T value) {
        Optional<BlockState> opState = this.blockstate.with(key, value);
        if (opState.isPresent()) {
            this.blockstate = opState.get();
            return true;
        }
        return false;
    }

    protected <T> Optional<KeyedData<T>> getKey(Class<? extends KeyedData<T>> data) {
        KeyedData<T> key = null;
        if (data.isAssignableFrom(OpenableKeyedData.class) && (this.blockstate.supports(Keys.IS_OPEN))) {
        } else if (data.isAssignableFrom(AttachableKeyedData.class) && this.blockstate.supports(Keys.IS_ATTACHED)) {
        } else if (data.isAssignableFrom(MultiDirectionalKeyedData.class) && this.blockstate.supports(Keys.CONNECTED_DIRECTIONS)) {
        }
        return Optional.ofNullable(key);
    }
}
