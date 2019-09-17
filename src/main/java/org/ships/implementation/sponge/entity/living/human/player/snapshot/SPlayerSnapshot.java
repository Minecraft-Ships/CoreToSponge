package org.ships.implementation.sponge.entity.living.human.player.snapshot;

import org.core.entity.EntityType;
import org.core.entity.living.human.AbstractHuman;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.ships.implementation.sponge.entity.SEntitySnapshot;
import org.ships.implementation.sponge.entity.SEntityType;

import java.util.UUID;

public class SPlayerSnapshot extends SEntitySnapshot<LivePlayer> implements PlayerSnapshot {

    protected int foodLevel;
    protected double exhaustionLevel;
    protected double saturationLevel;
    protected String name;
    protected boolean isSneaking;


    public SPlayerSnapshot(PlayerSnapshot snapshot) {
        super(snapshot);
    }

    public SPlayerSnapshot(LivePlayer entity) {
        super(entity);
    }

    @Override
    public boolean isViewingInventory() {
        return false;
    }

    @Override
    public EntityType<LivePlayer, PlayerSnapshot> getType() {
        return new SEntityType.SPlayerType();
    }

    @Override
    public PlayerInventory getInventory() {
        return null;
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new SPlayerSnapshot(this);
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
    public double getSaturationLevel() {
        return this.saturationLevel;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return getCreatedFrom().get().getUniqueId();
    }

    @Override
    public boolean isSneaking() {
        return this.isSneaking;
    }

    @Override
    public AbstractHuman setFood(int value) throws IndexOutOfBoundsException {
        this.foodLevel = value;
        return this;
    }

    @Override
    public AbstractHuman setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        this.exhaustionLevel = value;
        return this;
    }

    @Override
    public AbstractHuman setSaturationLevel(double value) throws IndexOutOfBoundsException {
        this.saturationLevel = value;
        return this;
    }

    @Override
    public AbstractHuman setSneaking(boolean sneaking) {
        this.isSneaking = sneaking;
        return this;
    }

    @Override
    public LivePlayer spawnEntity() {
        applyDefault(this.createdFrom);
        this.createdFrom.setExhaustionLevel(this.exhaustionLevel);
        this.createdFrom.setFood(this.foodLevel);
        this.createdFrom.setSaturationLevel(this.saturationLevel);
        this.createdFrom.setSneaking(this.isSneaking);
        return this.createdFrom;
    }
}
