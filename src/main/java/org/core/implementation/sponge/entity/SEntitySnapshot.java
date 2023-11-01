package org.core.implementation.sponge.entity;

import net.kyori.adventure.text.Component;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.LiveEntity;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.ExactPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public abstract class SEntitySnapshot<E extends LiveEntity> implements EntitySnapshot<E> {

    protected final Collection<EntitySnapshot<? extends LiveEntity>> passengers = new HashSet<>();
    protected final E createdFrom;
    protected double pitch;
    protected double yaw;
    protected double roll;
    protected SyncExactPosition position;
    protected boolean gravity;
    protected Vector3<Double> velocity;
    protected Component customName;
    protected boolean customNameVisible;
    protected boolean isOnGround;
    protected boolean isRemoved;

    public SEntitySnapshot(EntitySnapshot<E> snapshot) {
        this.init(snapshot);
        this.passengers.addAll(snapshot.getPassengers());
        this.createdFrom = snapshot.getCreatedFrom().orElse(null);
    }

    public SEntitySnapshot(E entity) {
        this.init(entity);
        entity.getPassengers().forEach(e -> this.passengers.add(e.createSnapshot()));
        this.createdFrom = entity;
    }

    private void init(Entity<?> entity) {
        this.customName = entity.getCustomNameComponent().orElse(null);
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
        if (this.isRemoved) {
            entity.remove();
        }
        return entity;
    }

    @Override
    public SyncExactPosition getPosition() {
        return this.position;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setPosition(Position<? extends Number> position) {
        if (position instanceof ExactPosition) {
            this.position = Position.toSync((ExactPosition) position);
        } else {
            this.position = Position.toSync(((BlockPosition) position).toExactPosition());
        }
        return this;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setGravity(boolean check) {
        this.gravity = check;
        return this;
    }

    @Override
    public double getPitch() {
        return this.pitch;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setPitch(double value) {
        this.pitch = value;
        return this;
    }

    @Override
    public double getYaw() {
        return this.yaw;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setYaw(double value) {
        this.yaw = value;
        return this;
    }

    @Override
    public double getRoll() {
        return this.roll;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setRoll(double value) {
        this.roll = value;
        return this;
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
    public EntitySnapshot<? extends LiveEntity> setVelocity(Vector3<Double> velocity) {
        this.velocity = velocity;
        return this;
    }

    @Override
    @Deprecated
    public Optional<AText> getCustomName() {
        return Optional.ofNullable(this.customName).map(AdventureText::new);
    }

    @Override
    public org.core.entity.Entity<EntitySnapshot<? extends LiveEntity>> setCustomName(@Nullable AText text) {
        this.customName = ((AdventureText) text).getComponent();
        return this;
    }

    @Override
    public Optional<Component> getCustomNameComponent() {
        return Optional.ofNullable(this.customName);
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setCustomName(@Nullable Component component) {
        this.customName = component;
        return this;
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.customNameVisible;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setCustomNameVisible(boolean visible) {
        this.customNameVisible = visible;
        return this;
    }

    @Override
    public Collection<EntitySnapshot<? extends LiveEntity>> getPassengers() {
        return this.passengers;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> addPassengers(Collection<? extends EntitySnapshot<? extends LiveEntity>> entities) {
        this.passengers.addAll(entities);
        return this;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> removePassengers(Collection<EntitySnapshot<? extends LiveEntity>> entities) {
        this.passengers.removeAll(entities);
        return this;
    }

    @Override
    public boolean isOnGround() {
        return this.isOnGround;
    }

    @Override
    public boolean isRemoved() {
        return this.isRemoved;
    }

    @Override
    public Optional<E> getCreatedFrom() {
        return Optional.ofNullable(this.createdFrom);
    }
}
