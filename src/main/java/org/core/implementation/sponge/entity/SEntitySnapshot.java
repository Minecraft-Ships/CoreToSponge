package org.core.implementation.sponge.entity;

import net.kyori.adventure.text.Component;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.LiveEntity;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.ExactPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.math.vector.Vector3d;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class SEntitySnapshot<E extends LiveEntity> implements EntitySnapshot<E> {

    protected final Collection<EntitySnapshot<? extends LiveEntity>> passengers = new HashSet<>();
    protected final E createdFrom;
    private final Map<Key<?>, Object> values = new ConcurrentHashMap<>();
    protected boolean isRemoved;

    protected SyncExactPosition position;
    protected double roll;
    protected double yaw;
    protected double pitch;

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
        this.position = entity.getPosition();
        this.isRemoved = entity.isRemoved();
        this.roll = entity.getRoll();
        this.pitch = entity.getPitch();
        this.yaw = entity.getYaw();
        if (entity instanceof SEntitySnapshot) {
            this.init((SEntitySnapshot<?>) entity);
            return;
        }
        if (entity instanceof SLiveEntity) {
            this.init((SLiveEntity) entity);
        }
    }

    private void init(SEntitySnapshot<?> snapshot) {
        this.values.putAll(snapshot.values);
    }

    private void init(SLiveEntity entity) {
        org.spongepowered.api.entity.Entity spongeEntity = entity.entity;
        var keys = spongeEntity
                .getKeys()
                .stream()
                .map(key -> Map.entry(key, this.getValue(spongeEntity, key)))
                .filter(entry -> entry.getValue().isPresent())
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().get()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.values.putAll(keys);
    }

    private <E> Optional<E> getValue(org.spongepowered.api.entity.Entity entity, Key<?> key) {
        Key<? extends Value<E>> castedKey = (Key<? extends Value<E>>) key;
        return entity.get(castedKey);
    }

    protected LiveEntity applyDefault(LiveEntity entity) {
        entity.setPosition(this.position);
        entity.setPitch(this.pitch);
        entity.setRoll(this.roll);
        entity.setYaw(this.yaw);
        if (entity instanceof SLiveEntity) {
            this.values.forEach((key, value) -> apply((SLiveEntity) entity, key, value));
        }
        if (this.isRemoved) {
            entity.remove();
        }
        return entity;
    }

    private <T> void apply(SLiveEntity entity, Key<?> key, T value) {
        Key<? extends Value<T>> castedKey = (Key<? extends Value<T>>) key;
        entity.entity.offer(castedKey, value);
    }

    private <T> void offer(Key<? extends Value<T>> key, T value) {
        if (value == null) {
            this.values.remove(key);
            return;
        }
        if (this.values.containsKey(key)) {
            this.values.replace(key, value);
            return;
        }
        this.values.put(key, value);
    }

    private <T> Optional<T> get(Key<? extends Value<T>> key) {
        return Optional.ofNullable((T) this.values.get(key));
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> addPassengers(Collection<? extends EntitySnapshot<? extends LiveEntity>> entities) {
        this.passengers.addAll(entities);
        return this;
    }

    @Override
    @Deprecated
    public Optional<AText> getCustomName() {
        return this.getCustomNameComponent().map(AdventureText::new);
    }

    @Override
    public org.core.entity.Entity<EntitySnapshot<? extends LiveEntity>> setCustomName(@Nullable AText text) {
        if (text == null) {
            return this.setCustomName((Component) null);
        }
        return this.setCustomName(text.asComponent());
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setCustomName(@Nullable Component component) {
        this.offer(Keys.CUSTOM_NAME, component);
        return this;
    }

    @Override
    public Optional<Component> getCustomNameComponent() {
        return this.get(Keys.CUSTOM_NAME);
    }

    @Override
    public Collection<EntitySnapshot<? extends LiveEntity>> getPassengers() {
        return this.passengers;
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
    public SyncExactPosition getPosition() {
        return this.position;
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
    public Vector3<Double> getVelocity() {
        return this
                .get(Keys.VELOCITY)
                .map(vector -> Vector3.valueOf(vector.x(), vector.y(), vector.z()))
                .orElse(Vector3.valueOf(0.0, 0, 0));
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setVelocity(Vector3<Double> velocity) {
        if (velocity == null) {
            this.offer(Keys.VELOCITY, null);
            return this;
        }
        this.offer(Keys.VELOCITY, new Vector3d(velocity.getX(), velocity.getY(), velocity.getZ()));
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
    public boolean hasGravity() {
        return this.get(Keys.IS_GRAVITY_AFFECTED).orElse(false);
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.get(Keys.IS_CUSTOM_NAME_VISIBLE).orElse(false);
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setCustomNameVisible(boolean visible) {
        this.offer(Keys.IS_CUSTOM_NAME_VISIBLE, visible);
        return this;
    }

    @Override
    public boolean isOnGround() {
        return this.get(Keys.IS_GRAVITY_AFFECTED).orElse(true);
    }

    @Override
    public boolean isRemoved() {
        return this.isRemoved;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> removePassengers(Collection<EntitySnapshot<? extends LiveEntity>> entities) {
        this.passengers.removeAll(entities);
        return this;
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> setGravity(boolean check) {
        this.offer(Keys.IS_GRAVITY_AFFECTED, check);
        return this;
    }

    @Override
    public boolean setPosition(@NotNull Position<? extends Number> position) {
        if (position instanceof ExactPosition) {
            this.position = Position.toSync((ExactPosition) position);
        } else {
            this.position = Position.toSync(position.toExactPosition());
        }
        return true;
    }

    @Override
    public Optional<E> getCreatedFrom() {
        return Optional.ofNullable(this.createdFrom);
    }
}
