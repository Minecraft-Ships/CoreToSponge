package org.core.implementation.sponge.inventory;

import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.core.world.position.block.BlockType;
import org.core.implementation.sponge.inventory.inventories.item.SItemStackSnapshot;
import org.core.implementation.sponge.world.position.block.SBlockType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Optional;

public class SItemType implements ItemType {

    private final org.spongepowered.api.item.ItemType type;

    public SItemType(org.spongepowered.api.item.ItemType type) {
        this.type = type;
    }

    @Override
    public ItemStackSnapshot getDefaultItemStack() {
        return new SItemStackSnapshot(ItemStack.builder().itemType(this.type).build().createSnapshot());
    }

    @Override
    public Optional<BlockType> getBlockType() {
        Optional<org.spongepowered.api.block.BlockType> opType = this.type.block();
        if (!opType.isPresent()) {
            return Optional.empty();
        }
        BlockType type = new SBlockType(opType.get());
        return Optional.of(type);
    }

    @Override
    public String getId() {
        return this.type.key(RegistryTypes.ITEM_TYPE).asString();
    }

    @Override
    public String getName() {
        return this.type.key(RegistryTypes.ITEM_TYPE).value();
    }
}
