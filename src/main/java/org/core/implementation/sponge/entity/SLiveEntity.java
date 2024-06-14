package org.core.implementation.sponge.entity;

import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.entity.Entity;
import org.core.entity.LiveEntity;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.implementation.sponge.world.position.SPosition;
import org.core.implementation.sponge.world.position.synced.SExactPosition;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3d;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class SLiveEntity implements LiveEntity {

    protected final org.spongepowered.api.entity.Entity entity;

    public SLiveEntity(org.spongepowered.api.entity.Entity entity) {
        this.entity = entity;
    }

    public org.spongepowered.api.entity.Entity getSpongeEntity() {
        return this.entity;
    }

    @Override
    public LiveEntity addPassengers(Collection<? extends LiveEntity> entities) {
        entities.forEach(e -> this.entity.passengers().add(((SLiveEntity) e).getSpongeEntity()));
        return this;
    }

    @Override
    public Entity<LiveEntity> setCustomName(@Nullable Component component) {
        if (component == null) {
            this.entity.remove(Keys.CUSTOM_NAME);
            return this;
        }
        this.entity.offer(Keys.CUSTOM_NAME, component);
        return this;
    }

    @Override
    public Optional<Component> getCustomNameComponent() {
        return this.entity.get(Keys.CUSTOM_NAME);
    }

    @Override
    public Collection<LiveEntity> getPassengers() {
        return this.entity
                .get(Keys.PASSENGERS)
                .map(list -> list
                        .stream()
                        .map(entity -> ((SpongePlatform) TranslateCore.getPlatform()).createEntityInstance(entity))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @Override
    public double getPitch() {
        return this.entity.rotation().x();
    }

    @Override
    public LiveEntity setPitch(double value) {
        this.entity.setRotation(new Vector3d(value, this.entity.rotation().y(), this.entity.rotation().z()));
        return this;
    }

    @Override
    public SyncExactPosition getPosition() {
        return new SExactPosition(this.entity.location());
    }

    @Override
    public double getRoll() {
        return this.entity.rotation().z();
    }

    @Override
    public LiveEntity setRoll(double value) {
        this.entity.setRotation(new Vector3d(this.entity.rotation().x(), this.entity.rotation().y(), value));
        return this;
    }

    @Override
    public Vector3<Double> getVelocity() {
        Vector3d vector3d = this.entity.get(Keys.VELOCITY).get();
        return Vector3.valueOf(vector3d.x(), vector3d.y(), vector3d.z());
    }

    @Override
    public LiveEntity setVelocity(Vector3<Double> velocity) {
        Vector3d vector = new Vector3d(velocity.getX(), velocity.getY(), velocity.getZ());
        this.entity.offer(Keys.VELOCITY, vector);
        return this;
    }

    @Override
    public double getYaw() {
        return this.entity.rotation().y();
    }

    @Override
    public LiveEntity setYaw(double value) {
        this.entity.setRotation(new Vector3d(this.entity.rotation().x(), value, this.entity.rotation().z()));
        return this;
    }

    @Override
    public boolean hasGravity() {
        return this.entity.get(Keys.IS_GRAVITY_AFFECTED).get();
    }

    @Override
    public boolean isCustomNameVisible() {
        Optional<Boolean> opBoolean = this.entity.get(Keys.IS_CUSTOM_NAME_VISIBLE);
        return opBoolean.orElse(false);
    }

    @Override
    public LiveEntity setCustomNameVisible(boolean visible) {
        this.entity.offer(Keys.IS_CUSTOM_NAME_VISIBLE, visible);
        return this;
    }

    @Override
    public boolean isOnGround() {
        return this.entity.get(Keys.ON_GROUND).get();
    }

    @Override
    public boolean isRemoved() {
        return this.entity.isRemoved();
    }

    @Override
    public LiveEntity removePassengers(Collection<LiveEntity> entities) {
        entities.forEach(e -> this.entity.passengers().remove(((SLiveEntity) e).getSpongeEntity()));
        return this;
    }

    @Override
    public LiveEntity setGravity(boolean check) {
        this.entity.offer(Keys.IS_GRAVITY_AFFECTED, check);
        return this;
    }

    @Override
    public boolean setPosition(@NotNull Position<? extends Number> position) {
        SPosition<? extends Number> position1 = (SPosition<? extends Number>) position;
        Location<?, ?> loc = position1.getSpongeLocation();
        if (loc.world() instanceof ServerWorld) {
            this.entity.transferToWorld((ServerWorld) loc.world());
        }
        return this.entity.setPosition(loc.position());
    }

    @Override
    public void remove() {
        this.entity.remove();
    }
}
