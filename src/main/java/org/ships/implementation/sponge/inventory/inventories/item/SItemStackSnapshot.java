package org.ships.implementation.sponge.inventory.inventories.item;

import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStack;
import org.core.text.Text;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SItemStackSnapshot implements org.core.inventory.item.stack.ItemStackSnapshot {

    protected ItemStackSnapshot item;

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
    public List<Text> getLore() {
        List<Text> list = new ArrayList<>();
        this.item.get(Keys.LORE).get().forEach(t -> list.add(SText.of(t)));
        return list;
    }

    @Override
    public List<AText> getLoreText() {
        return this.item.get(Keys.LORE).get().stream().map(AdventureText::new).collect(Collectors.toList());
    }

    @Override
    public ItemStack setLore(Collection<Text> lore) {
        List<Component> list = new ArrayList<>();
        lore.forEach(t -> list.add(((SText<?>) t).toSponge()));
        this.item = this.item.with(Keys.LORE, list).get();
        return this;
    }

    @Override
    public ItemStack setLoreText(Collection<AText> lore) {
        return null;
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
