package org.core.implementation.sponge.inventory.inventories.item;

import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.core.inventory.item.stack.LiveItemStack;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SLiveItemStack implements LiveItemStack {

    protected final org.spongepowered.api.item.inventory.ItemStack stack;

    public SLiveItemStack(org.spongepowered.api.item.inventory.ItemStack stack) {
        this.stack = stack;
    }

    public org.spongepowered.api.item.inventory.ItemStack getSponge() {
        return this.stack;
    }

    public SItemStackSnapshot toSpongeSnapshot() {
        return new SItemStackSnapshot(this.stack.createSnapshot());
    }

    @Override
    public ItemType getType() {
        SpongePlatform platform = ((SpongePlatform) TranslateCore.getPlatform());
        return platform.getItemType(this.stack.type().key(RegistryTypes.ITEM_TYPE).asString()).get();
    }

    @Override
    public int getQuantity() {
        return this.stack.quantity();
    }

    @Override
    public List<AText> getLoreText() {
        return this.stack.get(Keys.LORE).get().stream().map(AdventureText::new).collect(Collectors.toList());
    }


    @Override
    public ItemStack setLoreText(Collection<? extends AText> lore) {
        throw new RuntimeException("Not implemented yet");
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
