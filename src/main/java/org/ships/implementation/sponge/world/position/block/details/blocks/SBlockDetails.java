package org.ships.implementation.sponge.world.position.block.details.blocks;

import org.core.CorePlugin;
import org.core.world.position.impl.Position;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.*;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.ships.implementation.sponge.world.position.SBlockPosition;
import org.ships.implementation.sponge.world.position.block.SBlockType;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class SBlockDetails implements BlockDetails {

    public class STileEntityKeyedData implements TileEntityKeyedData {

        @Override
        public Optional<TileEntitySnapshot<? extends TileEntity>> getData() {
            return Optional.ofNullable(SBlockDetails.this.tileEntitySnapshot);
        }

        @Override
        public void setData(TileEntitySnapshot<? extends TileEntity> value) {
            SBlockDetails.this.tileEntitySnapshot = value;
        }
    }

    protected org.spongepowered.api.block.BlockState blockstate;
    protected TileEntitySnapshot<? extends TileEntity> tileEntitySnapshot;

    public SBlockDetails(org.spongepowered.api.block.BlockState state){
        this.blockstate = state;
        CorePlugin.getPlatform().getDefaultTileEntity(getType()).ifPresent(t -> tileEntitySnapshot = t);
    }

    public SBlockDetails(SBlockPosition position){
        this(position.getSpongeLocation().getBlock());
        if(position.getTileEntity().isPresent()){
            this.tileEntitySnapshot = position.getTileEntity().get().getSnapshot();
        }
    }

    @Override
    public BlockType getType() {
        return new SBlockType(this.blockstate.getType());
    }

    @Override
    public <P extends Position<Integer>> BlockSnapshot<P> createSnapshot(P position) {
        org.spongepowered.api.world.Location<World> loc = ((SBlockPosition)position).getSpongeLocation();
        return new SBlockSnapshot(org.spongepowered.api.block.BlockSnapshot.builder().blockState(this.blockstate).world(loc.getExtent().getProperties()).position(loc.getBlockPosition()).build());
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
        Optional<KeyedData<T>> opKey = getKey(data);
        if(opKey.isPresent()){
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

    @Override
    public BlockDetails createCopyOf() {
        return new SBlockDetails(this.blockstate.copy());
    }

    public BlockState getState() {
        return this.blockstate;
    }

    public <T> boolean setKey(Key key, T value) {
        Optional<BlockState> opState = this.blockstate.with(key, value);
        if(opState.isPresent()){
            this.blockstate = opState.get();
            return true;
        }
        return false;
    }

    private <T> Optional<KeyedData<T>> getKey(Class<? extends KeyedData<T>> data){
        KeyedData<T> key = null;
        if(data.isAssignableFrom(TileEntityKeyedData.class)){
            key = (KeyedData<T>) new STileEntityKeyedData();
        }else if(data.isAssignableFrom(OpenableKeyedData.class) && (this.blockstate.supports(Keys.OPEN))){
        }else if(data.isAssignableFrom(AttachableKeyedData.class) && this.blockstate.supports(Keys.ATTACHED)){
        }else if(data.isAssignableFrom(MultiDirectionalKeyedData.class) && this.blockstate.supports(Keys.CONNECTED_DIRECTIONS)){
        }
        return Optional.ofNullable(key);
    }
}
