package org.ships.implementation.sponge.entity;

import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.LiveEntity;
import org.core.text.Text;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.position.impl.sync.SyncPosition;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public abstract class SEntitySnapshot<E extends LiveEntity> implements EntitySnapshot<E> {

    protected double pitch;
    protected double yaw;
    protected double roll;
    protected SyncExactPosition position;
    protected boolean gravity;
    protected Vector3<Double> velocity;
    protected AdventureText customName;
    protected boolean customNameVisible;
    protected boolean isOnGround;
    protected Collection<EntitySnapshot<? extends LiveEntity>> passengers = new HashSet<>();
    protected E createdFrom;

    public SEntitySnapshot(EntitySnapshot<E> snapshot) {
        init(snapshot);
        this.passengers.addAll(snapshot.getPassengers());
        this.createdFrom = snapshot.getCreatedFrom().orElse(null);
    }

    public SEntitySnapshot(E entity) {
        init(entity);
        entity.getPassengers().forEach(e -> this.passengers.add(e.createSnapshot()));
        this.createdFrom = entity;
    }

    private void init(Entity<?> entity) {
        this.customName = (AdventureText) entity.getCustomName().orElse(null);
        this.customNameVisible = entity.isCustomNameVisible();
        this.gravity = entity.hasGravity();
        this.pitch = entity.getPitch();
        this.roll = entity.getRoll();
        this.yaw = entity.getYaw();
        this.position = entity.getPosition();
        this.velocity = entity.getVelocity();
        this.isOnGround = entity.isOnGround();
    }

    protected LiveEntity applyDefault(LiveEntity entity) {
        entity.setPosition(this.position);
        entity.setVelocity(this.velocity);
        entity.setCustomName(this.customName);
        entity.setCustomNameVisible(this.customNameVisible);
        entity.setGravity(this.gravity);
        entity.setPitch(this.pitch);
        entity.setRoll(this.roll);
        entity.setYaw(this.yaw);
        return entity;
    }

    @Override
    public SyncExactPosition getPosition() {
        return this.position;
    }

    @Override
    public boolean isOnGround() {
        return this.isOnGround;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setPitch(double value) {
        this.pitch = value;
        return this;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setYaw(double value) {
        this.yaw = value;
        return this;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setRoll(double value) {
        this.roll = value;
        return null;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setPosition(SyncPosition<? extends Number> position) {
        if (position instanceof SyncExactPosition) {
            this.position = (SyncExactPosition) position;
        } else {
            this.position = ((SyncBlockPosition) position).toExactPosition();
        }
        return null;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setGravity(boolean check) {
        this.gravity = check;
        return this;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setVelocity(Vector3<Double> velocity) {
        this.velocity = velocity;
        return this;
    }

    @Override
    @Deprecated
    public EntitySnapshot<? extends LiveEntity> setCustomName(Text text) {
        this.customName = (AdventureText) text.toAdventure();
        return this;
    }

    @Override
    public org.core.entity.Entity<EntitySnapshot<? extends LiveEntity>> setCustomName(@Nullable AText text) {
        this.customName = ((AdventureText) text);
        return this;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setCustomNameVisible(boolean visible) {
        this.customNameVisible = visible;
        return this;
    }

    @Override
    public double getPitch() {
        return this.pitch;
    }

    @Override
    public double getYaw() {
        return this.yaw;
    }

    @Override
    public double getRoll() {
        return this.roll;
    }

    @Override
    public boolean hasGravity() {
        return this.gravity;
    }

    @Override
    public Vector3<Double> getVelocity() {
        return this.velocity;
    }

    @Override
    @Deprecated
    public Optional<AText> getCustomName() {
        return Optional.ofNullable(this.customName);
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.customNameVisible;
    }

    @Override
    public Collection<EntitySnapshot<? extends LiveEntity>> getPassengers() {
        return this.passengers;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> addPassengers(Collection<EntitySnapshot<? extends LiveEntity>> entities) {
        this.passengers.addAll(entities);
        return this;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> removePassengers(Collection<EntitySnapshot<? extends LiveEntity>> entities) {
        this.passengers.removeAll(entities);
        return this;
    }

    @Override
    public Optional<E> getCreatedFrom() {
        return Optional.ofNullable(this.createdFrom);
    }
}
