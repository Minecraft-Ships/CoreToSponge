package org.ships.implementation.sponge.world.position.block.details.blocks.snapshot;

import net.kyori.adventure.text.Component;
import org.core.CorePlugin;
import org.core.text.Text;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.*;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.ships.implementation.sponge.world.position.SPosition;
import org.ships.implementation.sponge.world.position.block.details.blocks.StateDetails;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;
import org.ships.implementation.sponge.world.position.block.SBlockType;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SBlockSnapshot<P extends BlockPosition> implements BlockSnapshot<P>, StateDetails {

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
    protected Function<Location<? extends World<?>>, P> newPosition;

    public SBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot){
        this.snapshot = snapshot;
        this.newPosition = (Function<Location<? extends World<?>>, P>)SPosition.TO_SYNCED_BLOCK_POSITION;
        if(this.tileEntitySnapshot != null && this.tileEntitySnapshot instanceof SignTileEntitySnapshot){
            Optional<List<Component>> opLines = this.snapshot.get(Keys.SIGN_LINES);
            if(opLines.isPresent()) {
                List<Component> lines = opLines.get();
                for (int A = 0; A < lines.size(); A++) {
                    ((SignTileEntitySnapshot) this.tileEntitySnapshot).setLine(A, SText.of(lines.get(A)));
                }
            }
        }
    }

    public SBlockSnapshot(org.spongepowered.api.world.Location<? extends World<?>> location, Function<Location<? extends World<?>>, P> newInstance){
        this.snapshot = location.getWorld().createSnapshot(location.getBlockPosition());
        this.newPosition = newInstance;
        Optional<? extends org.spongepowered.api.block.entity.BlockEntity> opTileEntity = location.getBlockEntity();
        if(opTileEntity.isPresent()){
            ((SpongePlatform) CorePlugin.getPlatform()).createTileEntityInstance(opTileEntity.get()).ifPresent(te -> this.tileEntitySnapshot = te.getSnapshot());
        }
    }

    public SBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot, TileEntitySnapshot<? extends LiveTileEntity> tileEntity){
        this.snapshot = snapshot;
        this.newPosition = (Function<org.spongepowered.api.world.Location<? extends World<?>>, P>)SPosition.TO_SYNCED_BLOCK_POSITION;
        this.tileEntitySnapshot = tileEntity;
    }

    public org.spongepowered.api.block.BlockSnapshot getSnapshot(){
        return this.snapshot;
    }

    @Override
    public org.spongepowered.api.block.BlockState getState(){
        return this.getSnapshot().getState();
    }

    private <T> Optional<KeyedData<T>> getKey(Class<? extends KeyedData<T>> data){
        KeyedData<T> key = null;
        if(data.isAssignableFrom(TileEntityKeyedData.class)){
            key = (KeyedData<T>) new SBlockSnapshot.STileEntityKeyedData();
        }else if(data.isAssignableFrom(OpenableKeyedData.class) && (this.snapshot.supports(Keys.IS_OPEN))){
        }else if(data.isAssignableFrom(AttachableKeyedData.class) && this.snapshot.supports(Keys.IS_ATTACHED)){
        }else if(data.isAssignableFrom(MultiDirectionalKeyedData.class) && this.snapshot.supports(Keys.CONNECTED_DIRECTIONS)){
        }
        return Optional.ofNullable(key);
    }

    public <T> boolean setKey(Key<? extends Value<T>> key, T value) {
        Optional<org.spongepowered.api.block.BlockSnapshot> opState = this.snapshot.with(key, value);
        if(opState.isPresent()){
            this.snapshot = opState.get();
            return true;
        }
        return false;
    }

    @Override
    public BlockType getType() {
        return new SBlockType(this.snapshot.getState().getType());
    }

    @Override
    public <BP extends BlockPosition> BlockSnapshot<BP> createSnapshot(BP position) {
        SBlockPosition position1 = (SBlockPosition) position;
        org.spongepowered.api.block.BlockSnapshot snapshot = null;
        if(position1.getSpongeLocation().getWorld() instanceof ServerWorld){
            snapshot = org.spongepowered.api.block.BlockSnapshot.builder()
                    .from(this.snapshot)
                    .world(((ServerWorld) position1.getSpongeLocation().getWorld()).getProperties())
                    .position(position1.getSpongeLocation().getBlockPosition()).build();
        }else{
            snapshot = org.spongepowered.api.block.BlockSnapshot.builder()
                    .from(this.snapshot)
                    .position(position1.getSpongeLocation().getBlockPosition())
                    .build();
        }
        if(this.tileEntitySnapshot != null && this.tileEntitySnapshot instanceof SignTileEntitySnapshot){
            List<Component> lines = new ArrayList<>();
            for (Text text : ((SignTileEntitySnapshot) this.tileEntitySnapshot).getLines()){
                lines.add(((SText<? extends Component>)text).toSponge());
            }
            snapshot = snapshot.with(Keys.SIGN_LINES, lines).get();
        }
        return new SBlockSnapshot<>(snapshot, this.tileEntitySnapshot);
    }

    @Override
    public Optional<DirectionalData> getDirectionalData() {
        if(this.snapshot.supports(Keys.DIRECTION)){
            return Optional.of(new DirectionSnapshotWrapper(this));
        }
        return Optional.empty();
    }

    @Override
    public P getPosition() {
        return this.newPosition.apply(this.snapshot.getLocation().get());
    }

    @Override
    public BlockSnapshot<P> createCopyOf() {
        return new SBlockSnapshot<>(this.snapshot.copy(), this.tileEntitySnapshot.getSnapshot());
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

}
