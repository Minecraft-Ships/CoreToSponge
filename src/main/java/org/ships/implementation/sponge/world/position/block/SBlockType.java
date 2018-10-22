package org.ships.implementation.sponge.world.position.block;

import org.core.CorePlugin;
import org.core.inventory.item.ItemType;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.ships.implementation.sponge.platform.SpongePlatform;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SBlockType implements BlockType {

    protected org.spongepowered.api.block.BlockType type;

    public SBlockType(org.spongepowered.api.block.BlockType type){
        this.type = type;
    }

    @Override
    public BlockDetails getDefaultBlockDetails() {
        return ((SpongePlatform) CorePlugin.getPlatform()).createBlockDetailsInstance(this.type.getDefaultState()).get();
    }

    //THIS IS FOR 1.13
    @Override
    public Set<BlockType> getLike() {
        return new HashSet<>();
    }

    @Override
    public Optional<ItemType> getItemType() {
        return Optional.empty();
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
