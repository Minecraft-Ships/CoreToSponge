package org.core.implementation.sponge.entity.living.human.player.snapshot;

import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.implementation.sponge.entity.SEntitySnapshot;
import org.core.implementation.sponge.entity.SEntityType;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;

import java.util.UUID;

public class SPlayerSnapshot extends SEntitySnapshot<LivePlayer> implements PlayerSnapshot {

    protected final PlayerInventorySnapshot inventorySnapshot;
    protected int foodLevel;
    protected double exhaustionLevel;
    protected double saturationLevel;
    protected String name;
    protected boolean isSneaking;


    public SPlayerSnapshot(PlayerSnapshot snapshot) {
        super(snapshot);
        this.inventorySnapshot = snapshot.getInventory().createSnapshot();
    }

    public SPlayerSnapshot(LivePlayer entity) {
        super(entity);
        this.inventorySnapshot = entity.getInventory().createSnapshot();
    }

    @Override
    public boolean isViewingInventory() {
        return false;
    }

    @Override
    public PlayerInventory getInventory() {
        return this.inventorySnapshot;
    }

    @Override
    public SEntityType.SPlayerType getType() {
        return new SEntityType.SPlayerType();
    }

    @Override
    public boolean isOnGround() {
        return false;
    }

    @Override
    public int getFoodLevel() {
        return this.foodLevel;
    }

    @Override
    public double getExhaustionLevel() {
        return this.exhaustionLevel;
    }

    @Override
    public PlayerSnapshot setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        this.exhaustionLevel = value;
        return this;
    }

    @Override
    public double getSaturationLevel() {
        return this.saturationLevel;
    }

    @Override
    public PlayerSnapshot setSaturationLevel(double value) throws IndexOutOfBoundsException {
        this.saturationLevel = value;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isSneaking() {
        return this.isSneaking;
    }

    @Override
    public PlayerSnapshot setSneaking(boolean sneaking) {
        this.isSneaking = sneaking;
        return this;
    }

    @Override
    public PlayerSnapshot setFood(int value) throws IndexOutOfBoundsException {
        this.foodLevel = value;
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return this.getCreatedFrom().get().getUniqueId();
    }

    @Override
    public LivePlayer spawnEntity() {
        this.applyDefault(this.createdFrom);
        this.createdFrom.setExhaustionLevel(this.exhaustionLevel);
        this.createdFrom.setFood(this.foodLevel);
        this.createdFrom.setSaturationLevel(this.saturationLevel);
        this.createdFrom.setSneaking(this.isSneaking);
        return this.createdFrom;
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new SPlayerSnapshot(this);
    }

    @Override
    public LivePlayer teleportEntity(boolean keepInventory) {
        this.applyDefault(this.createdFrom);
        this.createdFrom.setSneaking(this.isSneaking);
        this.createdFrom.setExhaustionLevel(this.exhaustionLevel);
        this.createdFrom.setFood(this.foodLevel);
        this.createdFrom.setSaturationLevel(this.saturationLevel);
        if (!keepInventory) {
            this.inventorySnapshot.apply(this.createdFrom);
        }
        return this.createdFrom;
    }
}
