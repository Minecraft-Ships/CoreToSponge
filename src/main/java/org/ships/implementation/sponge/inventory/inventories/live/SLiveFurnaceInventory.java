package org.ships.implementation.sponge.inventory.inventories.live;

import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.ships.implementation.sponge.inventory.inventories.item.SLiveItemStack;
import org.ships.implementation.sponge.inventory.inventories.item.SItemStackSnapshot;
import org.ships.implementation.sponge.inventory.inventories.snapshot.SSnapshotFurnaceInventory;
import org.ships.implementation.sponge.world.position.SBlockPosition;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.slot.FuelSlot;
import org.spongepowered.api.item.inventory.slot.InputSlot;
import org.spongepowered.api.item.inventory.slot.OutputSlot;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;

import java.util.Optional;

public class SLiveFurnaceInventory implements FurnaceInventory {

    private static class FurnaceSlot implements Slot {

        protected org.spongepowered.api.item.inventory.Slot slot;

        public FurnaceSlot(org.spongepowered.api.item.inventory.Slot slot) {
            this.slot = slot;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> getItem() {
            Optional<org.spongepowered.api.item.inventory.ItemStack> opItem = this.slot.peek();
            if(opItem.isPresent()){
                return Optional.of(new SLiveItemStack(opItem.get()));
            }
            return Optional.empty();
        }

        @Override
        public Slot setItem(ItemStack stack) {
            if(stack == null){
                this.slot.set(org.spongepowered.api.item.inventory.ItemStack.empty());
                return this;
            }
            org.spongepowered.api.item.inventory.ItemStack stack1 = null;
            if(stack instanceof SLiveItemStack){
                stack1 = ((SLiveItemStack)stack).getSponge();
            }else if(stack instanceof SItemStackSnapshot){
                stack1 = ((SItemStackSnapshot)stack).getSponge().createStack();
            }
            this.slot.set(stack1);
            return this;
        }
    }

    protected org.spongepowered.api.block.tileentity.carrier.Furnace furnace;
    protected SLiveFurnaceInventory.FurnaceSlot fuel;
    protected SLiveFurnaceInventory.FurnaceSlot output;
    protected SLiveFurnaceInventory.FurnaceSlot input;

    public SLiveFurnaceInventory(org.spongepowered.api.block.tileentity.carrier.Furnace furnace){
        this.furnace = furnace;
        TileEntityInventory<TileEntityCarrier> inv = furnace.getInventory();
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
        org.spongepowered.api.world.Location<org.spongepowered.api.world.World> location = this.furnace.getLocation();
        return new SBlockPosition(location);
    }

    private static SLiveFurnaceInventory.FurnaceSlot[] getSlots(TileEntityInventory<TileEntityCarrier> inv){
        SLiveFurnaceInventory.FurnaceSlot[] slots = new SLiveFurnaceInventory.FurnaceSlot[3];
        for (Inventory inv1 : inv.slots()){
            org.spongepowered.api.item.inventory.Slot slot = (org.spongepowered.api.item.inventory.Slot) inv1;
            if(slot instanceof OutputSlot){
                slots[2] = new SLiveFurnaceInventory.FurnaceSlot(slot);
                continue;
            }
            if(slot instanceof FuelSlot){
                slots[1] = new SLiveFurnaceInventory.FurnaceSlot(slot);
                continue;
            }
            if(slot instanceof InputSlot){
                slots[0] = new SLiveFurnaceInventory.FurnaceSlot(slot);
                continue;
            }
        }
        return slots;
    }
}
