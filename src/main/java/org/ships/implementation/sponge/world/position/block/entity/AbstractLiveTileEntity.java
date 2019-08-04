package org.ships.implementation.sponge.world.position.block.entity;

import org.core.world.position.BlockPosition;
import org.core.world.position.block.entity.LiveTileEntity;
import org.ships.implementation.sponge.world.position.SBlockPosition;

public abstract class AbstractLiveTileEntity <T extends org.spongepowered.api.block.tileentity.TileEntity> implements LiveTileEntity{

    protected T tileEntity;

    public AbstractLiveTileEntity(T tileEntity){
        this.tileEntity = tileEntity;
    }

    public T getSpongeTileEntity(){
        return this.tileEntity;
    }

    @Override
    public BlockPosition getPosition() {
        return new SBlockPosition(this.tileEntity.getLocation());
    }
}
