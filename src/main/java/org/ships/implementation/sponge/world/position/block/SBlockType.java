package org.ships.implementation.sponge.world.position.block;

import org.core.CorePlugin;
import org.core.inventory.item.ItemType;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.grouptype.BlockGroup;
import org.ships.implementation.sponge.world.position.block.details.blocks.details.SBlockDetails;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SBlockType implements BlockType {

    protected org.spongepowered.api.block.BlockType type;

    public SBlockType(org.spongepowered.api.block.BlockType type){
        this.type = type;
    }

    @Override
    public BlockDetails getDefaultBlockDetails() {
        return new SBlockDetails(this.type.getDefaultState());
    }

    //THIS IS FOR 1.13
    @Override
    public Set<BlockGroup> getGroups() {
        return CorePlugin
                .getPlatform()
                .getBlockGroups()
                .stream()
                .filter(c -> {
                    return Stream
                            .of(c
                                    .getGrouped())
                            .anyMatch(bt -> bt
                                    .equals(this));
                }).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof SBlockType)){
            return false;
        }
        SBlockType type = (SBlockType)obj;
        return type.type.getId().equals(this.type.getId());
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
