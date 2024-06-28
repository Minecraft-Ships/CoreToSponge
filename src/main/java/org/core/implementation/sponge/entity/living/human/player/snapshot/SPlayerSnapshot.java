package org.core.implementation.sponge.entity.living.human.player.snapshot;

import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.implementation.sponge.entity.SEntitySnapshot;
import org.core.implementation.sponge.entity.SEntityType;
import org.core.implementation.sponge.entity.SLiveEntity;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;

import java.util.UUID;

public class SPlayerSnapshot extends SEntitySnapshot<LivePlayer> implements PlayerSnapshot {

    protected final PlayerInventorySnapshot inventorySnapshot;
    protected String name;


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
        return this.get(Keys.ON_GROUND).orElse(false);
    }

    @Override
    public int getFoodLevel() {
        return this.get(Keys.FOOD_LEVEL).orElse(20);
    }

    @Override
    public double getExhaustionLevel() {
        return this.get(Keys.EXHAUSTION).orElse(0.0);
    }

    @Override
    public PlayerSnapshot setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        this.offer(Keys.EXHAUSTION, value);
        return this;
    }

    @Override
    public double getSaturationLevel() {
        return this.get(Keys.SATURATION).orElse(0.0);
    }

    @Override
    public PlayerSnapshot setSaturationLevel(double value) throws IndexOutOfBoundsException {
        this.offer(Keys.SATURATION, value);
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isSneaking() {
        return this.get(Keys.IS_SNEAKING).orElse(false);
    }

    @Override
    public PlayerSnapshot setSneaking(boolean sneaking) {
        this.offer(Keys.IS_SNEAKING, sneaking);
        return this;
    }

    @Override
    public PlayerSnapshot setFood(int value) throws IndexOutOfBoundsException {
        this.offer(Keys.FOOD_LEVEL, value);
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return this.getCreatedFrom().get().getUniqueId();
    }

    @Override
    public LivePlayer spawnEntity() {
        this.applyDefault(this.createdFrom);
        this.values.forEach((key, value) -> this.set(this.createdFrom, key, value));
        return this.createdFrom;
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new SPlayerSnapshot(this);
    }

    private <T> void set(LivePlayer player, Key<? extends Value<?>> key, T value) {
        ((SLiveEntity) player).getSpongeEntity().offer((Key<? extends Value<T>>) key, value);
    }

    @Override
    public LivePlayer teleportEntity(boolean keepInventory) {
        this.applyDefault(this.createdFrom);
        this.values.forEach((key, value) -> this.set(this.createdFrom, key, value));
        if (!keepInventory) {
            this.inventorySnapshot.apply(this.createdFrom);
        }
        return this.createdFrom;
    }
}
