package org.ships.implementation.sponge.world.position.block.entity.furnace;

import org.core.exceptions.BlockNotSupported;
import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.world.position.BlockPosition;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntity;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntitySnapshot;
import org.core.world.position.block.entity.container.furnace.LiveFurnaceTileEntity;
import org.ships.implementation.sponge.inventory.inventories.snapshot.SSnapshotFurnaceInventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SFurnaceEntitySnapshot implements FurnaceTileEntitySnapshot {

    protected FurnaceInventorySnapshot inventory;

    public SFurnaceEntitySnapshot(){
        this.inventory = new SSnapshotFurnaceInventory();
    }

    public SFurnaceEntitySnapshot(FurnaceTileEntity tileEntity){
        this.inventory = tileEntity.getInventory().createSnapshot();
    }

    @Override
    public FurnaceTileEntity apply(BlockPosition position) throws BlockNotSupported {
        Optional<LiveTileEntity> opTE =position.getTileEntity();
        if(!opTE.isPresent()){
            throw new BlockNotSupported(position.getBlockType(), FurnaceTileEntitySnapshot.class.getSimpleName());
        }
        LiveTileEntity lte = opTE.get();
        if(!(lte instanceof LiveFurnaceTileEntity)){
            throw new BlockNotSupported(position.getBlockType(), FurnaceTileEntitySnapshot.class.getSimpleName());
        }
        LiveFurnaceTileEntity lfte = (LiveFurnaceTileEntity)lte;
        this.inventory.apply(lfte.getInventory());
        return this;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        //return Arrays.asList(BlockTypes.FURNACE.get());
        return new ArrayList<>();
    }

    @Override
    public FurnaceInventory getInventory() {
        return this.inventory;
    }

    @Override
    public FurnaceTileEntitySnapshot getSnapshot() {
        return new SFurnaceEntitySnapshot(this);
    }
}
