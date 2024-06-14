package org.core.implementation.sponge.inventory.inventories.item;

import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.core.inventory.item.stack.LiveItemStack;
import org.core.inventory.item.stack.data.ItemStackData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.*;
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
    public @NotNull ItemType getType() {
        SpongePlatform platform = ((SpongePlatform) TranslateCore.getPlatform());
        return platform.getItemType(this.stack.type().key(RegistryTypes.ITEM_TYPE).asString()).get();
    }

    @Override
    public int getQuantity() {
        return this.stack.quantity();
    }

    @Override
    public List<Component> getLore() {
        return this.stack.get(Keys.LORE).orElse(Collections.emptyList());
    }


    @Override
    public ItemStack setLore(Collection<? extends Component> lore) {
        this.stack.offer(Keys.LORE, new ArrayList<>(lore));
        return this;
    }

    @Override
    public ItemStack copy() {
        return new SLiveItemStack(this.stack.copy());
    }

    @Override
    public ItemStack copyWithQuantity(int quantity) {
        return new SLiveItemStack(
                org.spongepowered.api.item.inventory.ItemStack.builder().from(this.stack).quantity(quantity).build());
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
    public ItemStackSnapshot createSnapshot() {
        return new SItemStackSnapshot(this.stack.createSnapshot());
    }
}
