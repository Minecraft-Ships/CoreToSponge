package org.ships.implementation.sponge.world.position.block.entity;

import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.block.entity.LiveTileEntity;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;

public abstract class AbstractLiveTileEntity <T extends org.spongepowered.api.block.entity.BlockEntity> implements LiveTileEntity{

    protected T tileEntity;

    public AbstractLiveTileEntity(T tileEntity){
        this.tileEntity = tileEntity;
    }

    public T getSpongeTileEntity(){
        return this.tileEntity;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new SBlockPosition(this.tileEntity.getLocation());
    }
}
