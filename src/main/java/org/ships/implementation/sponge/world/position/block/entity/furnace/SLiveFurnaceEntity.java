package org.ships.implementation.sponge.world.position.block.entity.furnace;

import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntitySnapshot;
import org.core.world.position.block.entity.container.furnace.LiveFurnaceTileEntity;
import org.ships.implementation.sponge.inventory.inventories.live.SLiveFurnaceInventory;
import org.ships.implementation.sponge.world.position.block.entity.AbstractLiveTileEntity;

public class SLiveFurnaceEntity extends AbstractLiveTileEntity<org.spongepowered.api.block.entity.carrier.furnace.Furnace> implements LiveFurnaceTileEntity {

    public SLiveFurnaceEntity(org.spongepowered.api.block.entity.BlockEntity tileEntity){
        super((org.spongepowered.api.block.entity.carrier.furnace.Furnace)tileEntity);
    }

    public SLiveFurnaceEntity(org.spongepowered.api.block.entity.carrier.furnace.Furnace tileEntity) {
        super(tileEntity);
    }

    @Override
    public FurnaceInventory getInventory() {
        return new SLiveFurnaceInventory(this.getSpongeTileEntity());
    }

    @Override
    public FurnaceTileEntitySnapshot getSnapshot() {
        return new SFurnaceEntitySnapshot(this);
    }
}
