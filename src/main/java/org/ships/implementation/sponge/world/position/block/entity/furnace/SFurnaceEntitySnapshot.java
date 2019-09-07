package org.ships.implementation.sponge.world.position.block.entity.furnace;

import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntity;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntitySnapshot;
import org.core.world.position.block.entity.container.furnace.LiveFurnaceTileEntity;
import org.ships.implementation.sponge.inventory.inventories.snapshot.SSnapshotFurnaceInventory;

import java.util.Arrays;
import java.util.Collection;

public class SFurnaceEntitySnapshot implements FurnaceTileEntitySnapshot {

    protected FurnaceInventorySnapshot inventory;

    public SFurnaceEntitySnapshot(){
        this.inventory = new SSnapshotFurnaceInventory();
    }

    public SFurnaceEntitySnapshot(FurnaceTileEntity tileEntity){
        this.inventory = tileEntity.getInventory().createSnapshot();
    }

    @Override
    public LiveFurnaceTileEntity apply(LiveFurnaceTileEntity lfte) {
        this.inventory.apply(lfte.getInventory());
        return lfte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Arrays.asList(BlockTypes.FURNACE.get());
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
