package org.core.implementation.sponge.inventory.inventories.live;

import org.core.implementation.sponge.inventory.inventories.item.SItemStackSnapshot;
import org.core.implementation.sponge.inventory.inventories.item.SLiveItemStack;
import org.core.implementation.sponge.inventory.inventories.snapshot.SSnapshotFurnaceInventory;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.spongepowered.api.block.entity.carrier.CarrierBlockEntity;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.slot.FuelSlot;
import org.spongepowered.api.item.inventory.slot.InputSlot;
import org.spongepowered.api.item.inventory.slot.OutputSlot;
import org.spongepowered.api.item.inventory.type.BlockEntityInventory;

import java.util.Optional;

public class SLiveFurnaceInventory implements FurnaceInventory {

    private static class FurnaceSlot implements Slot {

        protected final org.spongepowered.api.item.inventory.Slot slot;

        private FurnaceSlot(org.spongepowered.api.item.inventory.Slot slot) {
            this.slot = slot;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.spongepowered.api.item.inventory.ItemStack item = this.slot.peek();
            if (item.equalTo(org.spongepowered.api.item.inventory.ItemStack.empty())) {
                return Optional.empty();
            }
            return Optional.of(new SLiveItemStack(item));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            if (stack==null) {
                this.slot.set(org.spongepowered.api.item.inventory.ItemStack.empty());
                return this;
            }
            org.spongepowered.api.item.inventory.ItemStack stack1 = null;
            if (stack instanceof SLiveItemStack) {
                stack1 = ((SLiveItemStack) stack).getSponge();
            } else if (stack instanceof SItemStackSnapshot) {
                stack1 = ((SItemStackSnapshot) stack).getSponge().createStack();
            }
            this.slot.set(stack1);
            return this;
        }
    }

    protected final org.spongepowered.api.block.entity.carrier.furnace.Furnace furnace;
    protected final SLiveFurnaceInventory.FurnaceSlot fuel;
    protected final SLiveFurnaceInventory.FurnaceSlot output;
    protected final SLiveFurnaceInventory.FurnaceSlot input;

    public SLiveFurnaceInventory(org.spongepowered.api.block.entity.carrier.furnace.Furnace furnace) {
        this.furnace = furnace;
        BlockEntityInventory<CarrierBlockEntity> inv = furnace.inventory();
        SLiveFurnaceInventory.FurnaceSlot[] slots = getSlots(inv);
        this.output = slots[2];
        this.fuel = slots[1];
        this.input = slots[0];
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

    private static SLiveFurnaceInventory.FurnaceSlot[] getSlots(Inventory inv) {
        SLiveFurnaceInventory.FurnaceSlot[] slots = new SLiveFurnaceInventory.FurnaceSlot[3];
        for (org.spongepowered.api.item.inventory.Slot slot : inv.slots()) {
            if (slot instanceof OutputSlot) {
                slots[2] = new SLiveFurnaceInventory.FurnaceSlot(slot);
                continue;
            }
            if (slot instanceof FuelSlot) {
                slots[1] = new SLiveFurnaceInventory.FurnaceSlot(slot);
                continue;
            }
            if (slot instanceof InputSlot) {
                slots[0] = new SLiveFurnaceInventory.FurnaceSlot(slot);
            }
        }
        return slots;
    }
}
