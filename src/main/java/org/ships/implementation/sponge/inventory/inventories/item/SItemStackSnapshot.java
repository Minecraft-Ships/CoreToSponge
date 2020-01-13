package org.ships.implementation.sponge.inventory.inventories.item;

import org.core.CorePlugin;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.item.ItemType;
import org.core.text.Text;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SItemStackSnapshot implements org.core.inventory.item.stack.ItemStackSnapshot {

    protected ItemStackSnapshot item;

    public SItemStackSnapshot(ItemStackSnapshot item){
        this.item = item;
    }

    public org.spongepowered.api.item.inventory.ItemStackSnapshot getSponge(){
        return this.item;
    }

    public SLiveItemStack toSpongeItem(){
        return new SLiveItemStack(this.item.createStack());
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
    public List<Text> getLore() {
        List<Text> list = new ArrayList<>();
        this.item.get(Keys.ITEM_LORE).get().stream().forEach(t -> list.add(new SText(t)));
        return list;
    }

    @Override
    public ItemStack setLore(Collection<Text> lore) {
        List<org.spongepowered.api.text.Text> list = new ArrayList<>();
        lore.stream().forEach(t -> list.add(((SText)t).toSponge()));
        this.item = this.item.with(Keys.ITEM_LORE, list).get();
        return this;
    }

    @Override
    public ItemStack copy() {
        return new SItemStackSnapshot(this.item.copy());
    }

    @Override
    public ItemStack copyWithQuantity(int quantity) {
        return new SItemStackSnapshot(org.spongepowered.api.item.inventory.ItemStack.builder().fromSnapshot(this.item).quantity(quantity).build().createSnapshot());
    }

    @Override
    public org.core.inventory.item.stack.ItemStackSnapshot createSnapshot() {
        return new SItemStackSnapshot(this.item.copy());
    }
}
