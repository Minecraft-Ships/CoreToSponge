package org.core.implementation.sponge.inventory.inventories.live;

import org.core.implementation.sponge.inventory.inventories.snapshot.SSnapshotFurnaceInventory;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.inventory.inventories.live.block.LiveFurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.inventory.parts.Slot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.spongepowered.api.block.entity.carrier.CarrierBlockEntity;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.slot.FuelSlot;
import org.spongepowered.api.item.inventory.slot.InputSlot;
import org.spongepowered.api.item.inventory.slot.OutputSlot;
import org.spongepowered.api.item.inventory.type.BlockEntityInventory;

public class SLiveFurnaceInventory implements LiveFurnaceInventory {

    protected final org.spongepowered.api.block.entity.carrier.furnace.Furnace furnace;
    protected final LiveSlot fuel;
    protected final LiveSlot output;
    protected final LiveSlot input;

    public SLiveFurnaceInventory(org.spongepowered.api.block.entity.carrier.furnace.Furnace furnace) {
        this.furnace = furnace;
        BlockEntityInventory<CarrierBlockEntity> inv = furnace.inventory();
        LiveSlot[] slots = getSlots(inv);
        this.output = slots[2];
        this.fuel = slots[1];
        this.input = slots[0];
    }

    private static LiveSlot[] getSlots(Inventory inv) {
        LiveSlot[] slots = new LiveSlot[3];
        for (org.spongepowered.api.item.inventory.Slot slot : inv.slots()) {
            if (slot instanceof OutputSlot) {
                slots[2] = new LiveSlot(slot);
                continue;
            }
            if (slot instanceof FuelSlot) {
                slots[1] = new LiveSlot(slot);
                continue;
            }
            if (slot instanceof InputSlot) {
                slots[0] = new LiveSlot(slot);
            }
        }
        return slots;
    }

    @Override
    public Slot getFuelSlot() {
        return this.fuel;
    }

    @Override
    public Slot getResultsSlot() {
        return this.output;
    }

    @Override
    public Slot getSmeltingSlot() {
        return this.input;
    }

    @Override
    public FurnaceInventorySnapshot createSnapshot() {
        return new SSnapshotFurnaceInventory(this);
    }

    @Override
    public SyncBlockPosition getPosition() {
        org.spongepowered.api.world.Location<? extends org.spongepowered.api.world.World<?, ?>, ?> location = this.furnace.location();
        return new SBlockPosition(location);
    }
}
