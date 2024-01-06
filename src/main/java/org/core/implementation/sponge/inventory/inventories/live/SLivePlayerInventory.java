package org.core.implementation.sponge.inventory.inventories.live;

import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.implementation.sponge.inventory.inventories.snapshot.SPlayerInventorySnapshot;
import org.core.inventory.inventories.live.entity.LivePlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;
import org.core.inventory.parts.*;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.registry.DefaultedRegistryReference;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SLivePlayerInventory implements LivePlayerInventory {

    public class PlayerArmorPart implements ArmorPart {

        @Override
        public Slot getHelmetSlot() {
            return SLivePlayerInventory.this.getSlot(EquipmentTypes.HEAD);
        }

        @Override
        public Slot getArmorSlot() {
            return SLivePlayerInventory.this.getSlot(EquipmentTypes.CHEST);
        }

        @Override
        public Slot getLeggingsSlot() {
            return SLivePlayerInventory.this.getSlot(EquipmentTypes.LEGS);
        }

        @Override
        public Slot getShoesSlot() {
            return SLivePlayerInventory.this.getSlot(EquipmentTypes.FEET);
        }
    }

    public class PlayerMainInventory implements MainPlayerInventory {

        @Override
        public Set<Slot> getSlots() {
            return SLivePlayerInventory.this.player
                    .getSpongeEntity()
                    .inventory()
                    .primary()
                    .slots()
                    .stream()
                    .map(LiveSlot::new)
                    .collect(Collectors.toSet());
        }
    }

    public class PlayerHotbar implements Hotbar {

        @Override
        public int getSelectedSlotPos() {
            return SLivePlayerInventory.this.player
                    .getSpongeEntity()
                    .inventory()
                    .primary()
                    .hotbar()
                    .selectedSlotIndex();
        }

        @Override
        public Set<Slot> getSlots() {
            return SLivePlayerInventory.this.player
                    .getSpongeEntity()
                    .inventory()
                    .primary()
                    .hotbar()
                    .slots()
                    .stream()
                    .map(LiveSlot::new)
                    .collect(Collectors.toSet());
        }
    }

    public static class PlayerGrid implements Grid2x2 {

        @Override
        public Set<Slot> getSlots() {
            //TODO
            return new HashSet<>();
        }
    }

    private final PlayerHotbar hotbar = new PlayerHotbar();
    private final PlayerArmorPart armorPart = new PlayerArmorPart();
    private final PlayerGrid grid = new PlayerGrid();
    private final PlayerMainInventory inventory = new PlayerMainInventory();
    protected SLivePlayer player;

    public SLivePlayerInventory(SLivePlayer player) {
        this.player = player;
    }

    @Override
    public Hotbar getHotbar() {
        return this.hotbar;
    }

    @Override
    public Grid2x2 getCraftingGrid() {
        return this.grid;
    }

    @Override
    public MainPlayerInventory getMainInventory() {
        return this.inventory;
    }

    @Override
    public PlayerInventorySnapshot createSnapshot() {
        return new SPlayerInventorySnapshot(this.player.getInventory());

    }

    @Override
    public ArmorPart getArmor() {
        return this.armorPart;
    }

    @Override
    public Slot getOffHoldingItem() {
        return new LiveSlot(player.getSpongeEntity().inventory().offhand());
    }

    @Override
    public Optional<LivePlayer> getAttachedEntity() {
        return Optional.of(this.player);
    }

    private Slot getSlot(DefaultedRegistryReference<EquipmentType> type) {
        return getOptionalSlot(type).orElseThrow(
                () -> new RuntimeException("Cannot find " + type.location() + " on player inv"));
    }

    private Optional<Slot> getOptionalSlot(DefaultedRegistryReference<EquipmentType> type) {
        return player.getSpongeEntity().inventory().armor().slot(type).map(LiveSlot::new);
    }
}
