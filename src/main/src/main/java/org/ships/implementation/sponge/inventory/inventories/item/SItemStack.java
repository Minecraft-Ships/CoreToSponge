package org.ships.implementation.sponge.inventory.inventories.item;

import org.core.CorePlugin;
import org.core.inventory.item.ItemStack;
import org.core.inventory.item.ItemType;
import org.ships.implementation.sponge.platform.SpongePlatform;

public class SItemStack implements ItemStack {

    protected org.spongepowered.api.item.inventory.ItemStack stack;

    public SItemStack(org.spongepowered.api.item.inventory.ItemStack stack){
        this.stack = stack;
    }

    public org.spongepowered.api.item.inventory.ItemStack getSponge(){
        return this.stack;
    }

    public SItemStackSnapshot toSpongeSnapshot(){
        return new SItemStackSnapshot(this.stack.createSnapshot());
    }

    @Override
    public ItemType getType() {
        SpongePlatform platform = ((SpongePlatform)CorePlugin.getPlatform());
        return platform.getItemType(this.stack.getType().getId()).get();
    }

    @Override
    public int getQuantity() {
        return this.stack.getQuantity();
    }

    @Override
    public ItemStack copy() {
        return new SItemStack(this.stack.copy());
    }

    @Override
    public ItemStack copyWithQuantity(int quantity) {
        return new SItemStack(org.spongepowered.api.item.inventory.ItemStack.builder().from(this.stack).quantity(quantity).build());

    }
}
