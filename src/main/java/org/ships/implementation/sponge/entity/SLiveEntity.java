package org.ships.implementation.sponge.entity;

import net.kyori.adventure.text.Component;
import org.core.CorePlugin;
import org.core.entity.LiveEntity;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.position.impl.sync.SyncPosition;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.ships.implementation.sponge.world.position.SPosition;
import org.ships.implementation.sponge.world.position.synced.SExactPosition;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3d;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class SLiveEntity implements LiveEntity {

    protected org.spongepowered.api.entity.Entity entity;

    public SLiveEntity(org.spongepowered.api.entity.Entity entity) {
        this.entity = entity;
    }

    public org.spongepowered.api.entity.Entity getSpongeEntity() {
        return this.entity;
    }

    @Override
    public boolean isOnGround() {
        return this.entity.get(Keys.ON_GROUND).get();
    }

    @Override
    public SyncExactPosition getPosition() {
        return new SExactPosition(this.entity.location());
    }

    @Override
    public LiveEntity setPitch(double value) {
        this.entity.setRotation(new Vector3d(value, this.entity.rotation().y(), this.entity.rotation().z()));
        return this;
    }

    @Override
    public LiveEntity setYaw(double value) {
        this.entity.setRotation(new Vector3d(this.entity.rotation().x(), value, this.entity.rotation().z()));
        return this;
    }

    @Override
    public LiveEntity setRoll(double value) {
        this.entity.setRotation(new Vector3d(this.entity.rotation().x(), this.entity.rotation().y(), value));
        return this;
    }

    @Override
    public LiveEntity setPosition(SyncPosition<? extends Number> position) {
        SPosition<? extends Number> position1 = (SPosition<? extends Number>) position;
        Location<?, ?> loc = position1.getSpongeLocation();
        if (loc.world() instanceof ServerWorld) {
            this.entity.transferToWorld((ServerWorld) loc.world());
        }
        this.entity.setPosition(loc.position());
        return this;
    }

    @Override
    public LiveEntity setGravity(boolean check) {
        this.entity.offer(Keys.IS_GRAVITY_AFFECTED, check);
        return this;
    }

    @Override
    public double getPitch() {
        return this.entity.rotation().x();
    }

    @Override
    public double getYaw() {
        return this.entity.rotation().y();
    }

    @Override
    public double getRoll() {
        return this.entity.rotation().z();
    }

    @Override
    public boolean hasGravity() {
        return this.entity.get(Keys.IS_GRAVITY_AFFECTED).get();
    }

    @Override
    public Collection<LiveEntity> getPassengers() {
        return this.entity.get(Keys.PASSENGERS)
                .map(list -> list
                        .stream()
                        .map(entity -> ((SpongePlatform) CorePlugin.getPlatform()).createEntityInstance(entity))
                        .collect(Collectors.toSet())).orElse(Collections.emptySet());
    }

    @Override
    public LiveEntity addPassengers(Collection<LiveEntity> entities) {
        entities.forEach(e -> this.entity.passengers().add(((SLiveEntity) e).getSpongeEntity()));
        return this;
    }

    @Override
    public LiveEntity removePassengers(Collection<LiveEntity> entities) {
        entities.forEach(e -> this.entity.passengers().remove(((SLiveEntity) e).getSpongeEntity()));
        return this;
    }

    @Override
    public LiveEntity setVelocity(Vector3<Double> velocity) {
        Vector3d vector = new Vector3d(velocity.getX(), velocity.getY(), velocity.getZ());
        this.entity.offer(Keys.VELOCITY, vector);
        return this;
    }

    @Override
    public LiveEntity setCustomName(org.core.text.Text text) {
        SText<?> sText = (SText<?>) text;
        this.entity.offer(Keys.DISPLAY_NAME, sText.toSponge());
        return this;
    }

    @Override
    public LiveEntity setCustomNameVisible(boolean visible) {
        this.entity.offer(Keys.IS_CUSTOM_NAME_VISIBLE, visible);
        return this;
    }

    @Override
    public Vector3<Double> getVelocity() {
        Vector3d vector3d = entity.get(Keys.VELOCITY).get();
        return Vector3.valueOf(vector3d.x(), vector3d.y(), vector3d.z());
    }

    @Override
    public Optional<org.core.text.Text> getCustomName() {
        Optional<Component> opValue = this.entity.get(Keys.DISPLAY_NAME);
        return opValue.map(SText::of);
    }

    @Override
    public boolean isCustomNameVisible() {
        Optional<Boolean> opBoolean = this.entity.get(Keys.IS_CUSTOM_NAME_VISIBLE);
        return opBoolean.orElse(false);
    }

    @Override
    public void remove() {
        this.entity.remove();
    }
}
