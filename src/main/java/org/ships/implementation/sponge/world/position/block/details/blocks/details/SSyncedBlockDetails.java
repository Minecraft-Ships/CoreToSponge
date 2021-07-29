package org.ships.implementation.sponge.world.position.block.details.blocks.details;

import org.core.CorePlugin;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.details.data.keyed.TileEntityKeyedData;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;

import java.util.Optional;

public class SSyncedBlockDetails extends SBlockDetails {

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

    protected TileEntitySnapshot<? extends TileEntity> tileEntitySnapshot;

    public SSyncedBlockDetails(org.spongepowered.api.block.BlockState state) {
        super(state);
        CorePlugin.getPlatform().getDefaultTileEntity(getType()).ifPresent(t -> tileEntitySnapshot = t);
    }

    public SSyncedBlockDetails(SBlockPosition position) {
        super(position.getSpongeLocation().block());
        if (position.getTileEntity().isPresent()) {
            this.tileEntitySnapshot = position.getTileEntity().get().getSnapshot();
        }
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
}
