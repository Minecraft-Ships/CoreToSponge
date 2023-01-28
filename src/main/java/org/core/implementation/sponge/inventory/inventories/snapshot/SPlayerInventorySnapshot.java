package org.core.implementation.sponge.inventory.inventories.snapshot;

import org.core.entity.living.human.player.LivePlayer;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;

public class SPlayerInventorySnapshot extends PlayerInventorySnapshot {
    public SPlayerInventorySnapshot(PlayerInventory inventory) {
        super(inventory);
    }

    @Override
    public PlayerInventorySnapshot createSnapshot() {
        return new SPlayerInventorySnapshot(this);
    }

    @Override
    public void apply(LivePlayer entity) {
        //TODO
    }
}
