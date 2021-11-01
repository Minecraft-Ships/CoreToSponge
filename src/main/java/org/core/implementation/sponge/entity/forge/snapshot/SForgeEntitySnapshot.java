package org.core.implementation.sponge.entity.forge.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.implementation.sponge.entity.SEntitySnapshot;
import org.core.implementation.sponge.entity.SEntityType;
import org.core.implementation.sponge.entity.forge.live.SForgeEntity;
import org.core.implementation.sponge.world.position.SPosition;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SForgeEntitySnapshot extends SEntitySnapshot<SForgeEntity> implements EntitySnapshot<SForgeEntity> {

    protected org.spongepowered.api.entity.EntityType<?> spongeType;

    public SForgeEntitySnapshot(EntitySnapshot<SForgeEntity> snapshot) {
        super(snapshot);
    }

    public SForgeEntitySnapshot(SForgeEntity entity) {
        super(entity);
    }

    @Override
    public SForgeEntity spawnEntity() {
        Location<? extends World<?, ?>, ?> location = ((SPosition<Double>) this.position).getSpongeLocation();
        Entity entity = location.world().createEntity(this.spongeType, location.position());
        SForgeEntity sEntity = new SForgeEntity(entity);
        this.applyDefault(sEntity);
        return sEntity;
    }

    @Override
    public EntityType<SForgeEntity, SForgeEntitySnapshot> getType() {
        return new SEntityType.SForgedEntityType(this.spongeType);
    }

    @Override
    public EntitySnapshot<SForgeEntity> createSnapshot() {
        return new SForgeEntitySnapshot(this);
    }

}
