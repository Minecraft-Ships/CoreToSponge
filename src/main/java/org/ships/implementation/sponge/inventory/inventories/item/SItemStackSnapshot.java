package org.ships.implementation.sponge.inventory.inventories.item;

import org.core.CorePlugin;
import org.core.inventory.item.ItemStack;
import org.core.inventory.item.ItemType;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class SItemStackSnapshot implements ItemStack {

    protected ItemStackSnapshot item;

    public SItemStackSnapshot(ItemStackSnapshot item){
        this.item = item;
    }

    public org.spongepowered.api.item.inventory.ItemStackSnapshot getSponge(){
        return this.item;
    }

    public SItemStack toSpongeItem(){
        return new SItemStack(this.item.createStack());
    }

    @Override
    public ItemType getType() {
        SpongePlatform platform = ((SpongePlatform) CorePlugin.getPlatform());
        return platform.getItemType(this.item.getType().getId()).get();
    }

    @Override
    public int getQuantity() {
        return this.item.getQuantity();
    }

    @Override
    public ItemStack copy() {
        return new SItemStackSnapshot(this.item.copy());
    }

    @Override
    public ItemStack copyWithQuantity(int quantity) {
        return new SItemStackSnapshot(org.spongepowered.api.item.inventory.ItemStack.builder().fromSnapshot(this.item).quantity(quantity).build().createSnapshot());
    }
}
