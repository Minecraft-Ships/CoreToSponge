package org.ships.implementation.sponge.inventory.inventories.item;

import org.core.CorePlugin;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.core.inventory.item.stack.LiveItemStack;
import org.core.text.Text;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.data.key.Keys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SLiveItemStack implements LiveItemStack {

    protected org.spongepowered.api.item.inventory.ItemStack stack;

    public SLiveItemStack(org.spongepowered.api.item.inventory.ItemStack stack){
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
    public List<Text> getLore() {
        List<Text> list = new ArrayList<>();
        this.stack.get(Keys.ITEM_LORE).get().stream().forEach(t -> list.add(new SText(t)));
        return list;
    }

    @Override
    public ItemStack setLore(Collection<Text> lore) {
        List<org.spongepowered.api.text.Text> list = new ArrayList<>();
        lore.stream().forEach(t -> list.add(((SText)t).toSponge()));
        this.stack.offer(Keys.ITEM_LORE, list);
        return this;
    }

    @Override
    public ItemStack copy() {
        return new SLiveItemStack(this.stack.copy());
    }

    @Override
    public ItemStack copyWithQuantity(int quantity) {
        return new SLiveItemStack(org.spongepowered.api.item.inventory.ItemStack.builder().from(this.stack).quantity(quantity).build());
    }

    @Override
    public ItemStackSnapshot createSnapshot() {
        return new SItemStackSnapshot(this.stack.createSnapshot());
    }
}
