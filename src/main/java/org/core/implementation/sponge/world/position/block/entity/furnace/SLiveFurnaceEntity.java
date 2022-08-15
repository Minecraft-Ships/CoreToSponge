package org.core.implementation.sponge.world.position.block.entity.furnace;

import org.core.implementation.sponge.inventory.inventories.live.SLiveFurnaceInventory;
import org.core.implementation.sponge.world.position.block.entity.AbstractLiveTileEntity;
import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.inventory.inventories.live.block.LiveFurnaceInventory;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntitySnapshot;
import org.core.world.position.block.entity.container.furnace.LiveFurnaceTileEntity;
import org.spongepowered.api.block.entity.carrier.furnace.Furnace;

public class SLiveFurnaceEntity extends AbstractLiveTileEntity<Furnace> implements LiveFurnaceTileEntity {

    public SLiveFurnaceEntity(org.spongepowered.api.block.entity.BlockEntity tileEntity) {
        super((org.spongepowered.api.block.entity.carrier.furnace.Furnace) tileEntity);
    }

    public SLiveFurnaceEntity(org.spongepowered.api.block.entity.carrier.furnace.Furnace tileEntity) {
        super(tileEntity);
    }

    @Override
    public LiveFurnaceInventory getInventory() {
        return new SLiveFurnaceInventory(this.getSpongeTileEntity());
    }

    @Override
    public FurnaceTileEntitySnapshot getSnapshot() {
        return new SFurnaceEntitySnapshot(this);
    }
}
