package org.core.implementation.sponge.inventory.inventories.live;

import org.core.implementation.sponge.inventory.inventories.item.SItemStackSnapshot;
import org.core.implementation.sponge.inventory.inventories.item.SLiveItemStack;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;

import java.util.Optional;

public class LiveSlot implements Slot {

    protected final org.spongepowered.api.item.inventory.Slot slot;

    public LiveSlot(org.spongepowered.api.item.inventory.Slot slot) {
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
        if (stack == null) {
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
