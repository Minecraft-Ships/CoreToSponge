package org.core.implementation.sponge.world.position.block.entity.furnace;

import org.core.implementation.sponge.inventory.inventories.snapshot.SSnapshotFurnaceInventory;
import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntity;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntitySnapshot;
import org.core.world.position.block.entity.container.furnace.LiveFurnaceTileEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public class SFurnaceEntitySnapshot implements FurnaceTileEntitySnapshot {

    protected final FurnaceInventorySnapshot inventory;

    public SFurnaceEntitySnapshot() {
        this.inventory = new SSnapshotFurnaceInventory();
    }

    public SFurnaceEntitySnapshot(@SuppressWarnings("TypeMayBeWeakened") FurnaceTileEntity tileEntity) {
        this.inventory = tileEntity.getInventory().createSnapshot();
    }

    @Override
    public LiveFurnaceTileEntity apply(LiveFurnaceTileEntity lfte) {
        this.inventory.apply(lfte.getInventory());
        return lfte;
    }

    @Override
    public Stream<BlockType> getApplicableBlocks() {
        return Stream.of(BlockTypes.FURNACE);
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
