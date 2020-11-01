package org.ships.implementation.sponge.world.position.block;

import org.array.utils.ArrayUtils;
import org.core.CorePlugin;
import org.core.inventory.item.ItemType;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.grouptype.BlockGroup;
import org.ships.implementation.sponge.world.position.block.details.blocks.details.SBlockDetails;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                .filter(c -> Arrays.asList(c
                        .getGrouped()).contains(this)).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof SBlockType)){
            return false;
        }
        SBlockType type = (SBlockType)obj;
        return type.type.equals(this.type);
    }

    @Override
    public Optional<ItemType> getItemType() {
        return Optional.empty();
    }

    @Override
    public String getId() {
        return this.type.getKey().asString();
    }

    @Override
    public String getName() {
        return this.type.getKey().getValue();
    }
}
