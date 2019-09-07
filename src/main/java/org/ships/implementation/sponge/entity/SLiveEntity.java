package org.ships.implementation.sponge.entity;

import com.flowpowered.math.vector.Vector3d;
import org.core.CorePlugin;
import org.core.entity.LiveEntity;
import org.core.vector.types.Vector3Double;
import org.core.world.position.BlockPosition;
import org.core.world.position.ExactPosition;
import org.core.world.position.Position;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.SWorldExtent;
import org.ships.implementation.sponge.world.position.SExactPosition;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;

import java.util.Collection;
import java.util.HashSet;

public abstract class SLiveEntity implements LiveEntity {

    protected org.spongepowered.api.entity.Entity entity;

    public SLiveEntity(org.spongepowered.api.entity.Entity entity){
        this.entity = entity;
    }

    public org.spongepowered.api.entity.Entity getSpongeEntity(){
        return this.entity;
    }

    @Override
    public ExactPosition getPosition() {
        return new SExactPosition(this.entity.getLocation());
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
    public LiveEntity setPosition(Position<? extends Number> position) {
        ExactPosition position1 = position instanceof ExactPosition ? (ExactPosition)position : ((BlockPosition)position).toExactPosition();
        this.entity.setLocation(new org.spongepowered.api.world.Location<>(((SWorldExtent)position1.getWorld()).getSpongeWorld(), position1.getX(), position1.getY(), position1.getZ()));
        return this;
    }

    @Override
    public LiveEntity setGravity(boolean check) {
        this.entity.offer(Keys.HAS_GRAVITY, check);
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
        return this.entity.get(Keys.HAS_GRAVITY).get();
    }

    @Override
    public Collection<LiveEntity> getPassengers() {
        Collection<LiveEntity> entities = new HashSet<>();
        this.entity.get(Keys.PASSENGERS).get().stream().forEach(uuid -> Sponge.getServer().getWorlds().stream().forEach(w -> w.getEntity(uuid).ifPresent(e -> entities.add(((SpongePlatform)CorePlugin.getPlatform()).createEntityInstance(e)))));
        return entities;
    }

    @Override
    public LiveEntity addPassengers(Collection<LiveEntity> entities) {
        return null;
    }

    @Override
    public LiveEntity removePassengers(Collection<LiveEntity> entities) {
        return null;
    }

    @Override
    public LiveEntity setVelocity(Vector3Double velocity) {
        return null;
    }

    @Override
    public LiveEntity setCustomName(org.core.text.Text text) {
        return null;
    }

    @Override
    public LiveEntity setCustomNameVisible(boolean visible) {
        return null;
    }

    @Override
    public Vector3Double getVelocity() {
        return null;
    }

    @Override
    public org.core.text.Text getCustomName() {
        return null;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public void remove() {
        this.entity.remove();
    }
}
