package org.core.implementation.sponge.world.position.block;

import org.core.TranslateCore;
import org.core.inventory.item.ItemType;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.grouptype.BlockGroup;
import org.core.implementation.sponge.world.position.block.details.blocks.details.SSyncedBlockDetails;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SBlockType implements BlockType {

    protected org.spongepowered.api.block.BlockType type;

    public SBlockType(org.spongepowered.api.block.BlockType type) {
        this.type = type;
    }

    @Override
    public BlockDetails getDefaultBlockDetails() {
        return new SSyncedBlockDetails(this.type.defaultState());
    }

    //THIS IS FOR 1.13
    @Override
    public Set<BlockGroup> getGroups() {
        return TranslateCore
                .getPlatform()
                .getBlockGroups()
                .stream()
                .filter(c -> Arrays.asList(c
                        .getGrouped()).contains(this)).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SBlockType)) {
            return false;
        }
        SBlockType type = (SBlockType) obj;
        return type.type.equals(this.type);
    }

    @Override
    public Optional<ItemType> getItemType() {
        return Optional.empty();
    }

    @Override
    public String getId() {
        return this.type.key(RegistryTypes.BLOCK_TYPE).asString();
    }

    @Override
    public String getName() {
        return this.type.key(RegistryTypes.BLOCK_TYPE).value();
    }
}
