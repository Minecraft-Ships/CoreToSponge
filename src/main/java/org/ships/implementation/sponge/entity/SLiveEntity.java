package org.ships.implementation.sponge.entity;

import net.kyori.adventure.text.Component;
import org.core.CorePlugin;
import org.core.entity.LiveEntity;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.position.impl.sync.SyncPosition;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.ships.implementation.sponge.world.SWorldExtent;
import org.ships.implementation.sponge.world.position.synced.SExactPosition;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.math.vector.Vector3d;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public abstract class SLiveEntity implements LiveEntity {

    protected org.spongepowered.api.entity.Entity entity;

    public SLiveEntity(org.spongepowered.api.entity.Entity entity){
        this.entity = entity;
    }

    public org.spongepowered.api.entity.Entity getSpongeEntity(){
        return this.entity;
    }

    @Override
    public boolean isOnGround() {
        return this.entity.get(Keys.ON_GROUND).get();
    }

    @Override
    public SyncExactPosition getPosition() {
        return new SExactPosition((Location<? extends World<?>>) this.entity.getLocation());
    }

    @Override
    public LiveEntity setPitch(double value) {
        this.entity.setRotation(new Vector3d(value, this.entity.getRotation().getY(), this.entity.getRotation().getZ()));
        return this;
    }

    @Override
    public LiveEntity setYaw(double value) {
        this.entity.setRotation(new Vector3d(this.entity.getRotation().getX(), value, this.entity.getRotation().getZ()));
        return this;
    }

    @Override
    public LiveEntity setRoll(double value) {
        this.entity.setRotation(new Vector3d(this.entity.getRotation().getX(), this.entity.getRotation().getY(), value));
        return this;
    }

    @Override
    public LiveEntity setPosition(SyncPosition<? extends Number> position) {
        SyncExactPosition position1 = position instanceof SyncExactPosition ? (SyncExactPosition)position : ((SyncBlockPosition)position).toExactPosition();
        this.entity.setLocation(((SWorldExtent)position1.getWorld()).getSpongeWorld().getLocation(position1.getX(), position1.getY(), position1.getZ()));
        return this;
    }

    @Override
    public LiveEntity setGravity(boolean check) {
        this.entity.offer(Keys.IS_GRAVITY_AFFECTED, check);
        return this;
    }

    @Override
    public double getPitch() {
        return this.entity.getRotation().getX();
    }

    @Override
    public double getYaw() {
        return this.entity.getRotation().getY();
    }

    @Override
    public double getRoll() {
        return this.entity.getRotation().getZ();
    }

    @Override
    public boolean hasGravity() {
        return this.entity.get(Keys.IS_GRAVITY_AFFECTED).get();
    }

    @Override
    public Collection<LiveEntity> getPassengers() {
        Collection<LiveEntity> entities = new HashSet<>();
        this.entity.get(Keys.PASSENGERS).ifPresent(list -> list.forEach(entity -> Sponge.getServer().getWorldManager().getWorlds().forEach(w -> w.getEntity(entity.getUniqueId()).ifPresent(e -> entities.add(((SpongePlatform)CorePlugin.getPlatform()).createEntityInstance(e))))));
        return entities;
    }

    @Override
    public LiveEntity addPassengers(Collection<LiveEntity> entities) {
        entities.forEach(e -> this.entity.passengers().add(((SLiveEntity)e).getSpongeEntity()));
        return this;
    }

    @Override
    public LiveEntity removePassengers(Collection<LiveEntity> entities) {
        entities.forEach(e -> this.entity.passengers().remove(((SLiveEntity)e).getSpongeEntity()));
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
        SText<?> sText = (SText<?>)text;
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
        return Vector3.valueOf(vector3d.getX(), vector3d.getY(), vector3d.getZ());
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
