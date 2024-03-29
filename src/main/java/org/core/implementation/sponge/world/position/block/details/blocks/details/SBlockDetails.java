package org.core.implementation.sponge.world.position.block.details.blocks.details;

import org.core.implementation.sponge.world.position.block.SBlockType;
import org.core.implementation.sponge.world.position.block.details.blocks.StateDetails;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.AttachableKeyedData;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.details.data.keyed.MultiDirectionalKeyedData;
import org.core.world.position.block.details.data.keyed.OpenableKeyedData;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;

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
    public Optional<DirectionalData> getDirectionalData() {
        if (this.blockstate.supports(Keys.DIRECTION)) {
            return Optional.of(new DirectionStateWrapper(this));
        }
        return Optional.empty();
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

    @Override
    public BlockState getState() {
        return this.blockstate;
    }

    public <T> boolean setKey(Key<? extends Value<T>> key, T value) {
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
        } else if (data.isAssignableFrom(MultiDirectionalKeyedData.class) && this.blockstate.supports(
                Keys.CONNECTED_DIRECTIONS)) {
        }
        return Optional.ofNullable(key);
    }
}
