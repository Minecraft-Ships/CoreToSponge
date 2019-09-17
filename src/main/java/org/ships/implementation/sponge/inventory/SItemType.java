package org.ships.implementation.sponge.inventory;

import org.core.inventory.item.ItemStack;
import org.core.inventory.item.ItemType;
import org.core.world.position.block.BlockType;
import org.ships.implementation.sponge.inventory.inventories.item.SItemStackSnapshot;
import org.ships.implementation.sponge.world.position.block.SBlockType;

import java.util.Optional;

public class SItemType implements ItemType {

    org.spongepowered.api.item.ItemType type;

    public SItemType(org.spongepowered.api.item.ItemType type){
        this.type = type;
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return new SItemStackSnapshot(this.type.getTemplate());
    }

    @Override
    public Optional<BlockType> getBlockType() {
        Optional<org.spongepowered.api.block.BlockType> opType = this.type.getBlock();
        if(!opType.isPresent()){
            return Optional.empty();
        }
        BlockType type = new SBlockType(opType.get());
        return Optional.of(type);
    }

    @Override
    public String getId() {
        return this.type.getId();
    }

    @Override
    public String getName() {
        return this.type.getName();
    }
}
