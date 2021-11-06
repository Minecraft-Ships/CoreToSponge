package org.core.implementation.sponge.inventory.inventories.item;

import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.item.stack.data.ItemStackData;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SItemStackSnapshot implements org.core.inventory.item.stack.ItemStackSnapshot {

    protected final ItemStackSnapshot item;

    public SItemStackSnapshot(ItemStackSnapshot item) {
        this.item = item;
    }

    public org.spongepowered.api.item.inventory.ItemStackSnapshot getSponge() {
        return this.item;
    }

    public SLiveItemStack toSpongeItem() {
        return new SLiveItemStack(this.item.createStack());
    }

    @Override
    public ItemType getType() {
        SpongePlatform platform = ((SpongePlatform) TranslateCore.getPlatform());
        return platform.getItemType(this.item.type().key(RegistryTypes.ITEM_TYPE).asString()).get();
    }

    @Override
    public int getQuantity() {
        return this.item.quantity();
    }

    @Override
    public List<AText> getLoreText() {
        return this.item.get(Keys.LORE).get().stream().map(AdventureText::new).collect(Collectors.toList());
    }

    @Override
    public ItemStack setLoreText(Collection<? extends AText> lore) {
        throw new RuntimeException("Not implemented yet");
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
    public Optional<ItemStackData> getStackData() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setStackData(ItemStackData data) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public org.core.inventory.item.stack.ItemStackSnapshot createSnapshot() {
        return new SItemStackSnapshot(this.item.copy());
    }
}
