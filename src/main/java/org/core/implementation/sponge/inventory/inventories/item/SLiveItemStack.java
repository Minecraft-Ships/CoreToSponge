package org.core.implementation.sponge.inventory.inventories.item;

import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.implementation.sponge.text.SText;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.core.inventory.item.stack.LiveItemStack;
import org.core.text.Text;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SLiveItemStack implements LiveItemStack {

    protected org.spongepowered.api.item.inventory.ItemStack stack;

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
    public List<Text> getLore() {
        List<Text> list = new ArrayList<>();
        this.stack.get(Keys.LORE).get().forEach(t -> list.add(SText.of(t)));
        return list;
    }

    @Override
    public List<AText> getLoreText() {
        return this.stack.get(Keys.LORE).get().stream().map(AdventureText::new).collect(Collectors.toList());
    }

    @Override
    public ItemStack setLore(Collection<Text> lore) {
        List<Component> list = new ArrayList<>();
        lore.forEach(t -> list.add(((SText<?>) t).toSponge()));
        this.stack.offer(Keys.LORE, list);
        return this;
    }

    @Override
    public ItemStack setLoreText(Collection<AText> lore) {
        return null;
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
