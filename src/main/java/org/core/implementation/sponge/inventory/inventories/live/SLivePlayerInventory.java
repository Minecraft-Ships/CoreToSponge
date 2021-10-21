package org.core.implementation.sponge.inventory.inventories.live;

import org.core.entity.living.human.player.LivePlayer;
import org.core.inventory.inventories.live.entity.LivePlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;
import org.core.inventory.parts.*;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;

import java.util.Optional;
import java.util.Set;

public class SLivePlayerInventory implements LivePlayerInventory {

    public class PlayerHotbar implements Hotbar {

        @Override
        public int getSelectedSlotPos() {
            return SLivePlayerInventory.this.player.getInventory().getHotbar().getSelectedSlotPos();
        }

        @Override
        public Set<Slot> getSlots() {
            return SLivePlayerInventory.this.player.getInventory().getHotbar().getSlots();
        }
    }

    protected SLivePlayer player;
    protected PlayerHotbar hotbar = new PlayerHotbar();

    @Override
    public Hotbar getHotbar() {
        return this.hotbar;
    }

    @Override
    public Grid2x2 getCraftingGrid() {
        return null;
    }

    @Override
    public MainPlayerInventory getMainInventory() {
        return null;
    }

    @Override
    public PlayerInventorySnapshot createSnapshot() {
        return null;
    }

    @Override
    public Optional<LivePlayer> getAttachedEntity() {
        return Optional.of(this.player);
    }

    @Override
    public ArmorPart getArmor() {
        return null;
    }

    @Override
    public Slot getOffHoldingItem() {
        return null;
    }
}
