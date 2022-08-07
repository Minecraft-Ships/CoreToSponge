package org.core.implementation.sponge.inventory.inventories.live;

import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.inventory.inventories.live.entity.LivePlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;
import org.core.inventory.parts.*;

import java.util.Optional;
import java.util.Set;

public class SLivePlayerInventory implements LivePlayerInventory {

    protected final PlayerHotbar hotbar = new PlayerHotbar();
    protected SLivePlayer player;

    @Override
    public Hotbar getHotbar() {
        return this.hotbar;
    }

    @Override
    public Grid2x2 getCraftingGrid() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public MainPlayerInventory getMainInventory() {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public PlayerInventorySnapshot createSnapshot() {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public Optional<LivePlayer> getAttachedEntity() {
        return Optional.of(this.player);
    }

    @Override
    public ArmorPart getArmor() {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public Slot getOffHoldingItem() {
        throw new RuntimeException("Not implemented yet");

    }

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
}
