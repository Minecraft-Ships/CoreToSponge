package org.core.implementation.sponge.inventory.inventories.snapshot;

import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.inventory.parts.snapshot.SlotSnapshot;

public class SSnapshotFurnaceInventory extends FurnaceInventorySnapshot {

    public SSnapshotFurnaceInventory(){
        this.smeltingSlot = new SlotSnapshot(1, null);
        this.fuelSlot = new SlotSnapshot(2, null);
        this.resultsSlot = new SlotSnapshot(3, null);
    }

    public SSnapshotFurnaceInventory(FurnaceInventory inventory){
        this.fuelSlot = new SlotSnapshot(inventory.getFuelSlot());
        this.smeltingSlot = new SlotSnapshot(inventory.getSmeltingSlot());
        this.resultsSlot = new SlotSnapshot(inventory.getResultsSlot());
    }

    @Override
    public FurnaceInventorySnapshot createSnapshot() {
        return new SSnapshotFurnaceInventory(this);
    }
}
