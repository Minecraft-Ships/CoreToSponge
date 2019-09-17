package org.ships.implementation.sponge.entity.forge.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.ships.implementation.sponge.entity.SEntitySnapshot;
import org.ships.implementation.sponge.entity.SEntityType;
import org.ships.implementation.sponge.entity.forge.live.SForgeEntity;
import org.ships.implementation.sponge.world.position.SExactPosition;

public class SForgeEntitySnapshot extends SEntitySnapshot<SForgeEntity> implements EntitySnapshot<SForgeEntity> {

    protected org.spongepowered.api.entity.EntityType spongeType;

    public SForgeEntitySnapshot(EntitySnapshot<SForgeEntity> snapshot) {
        super(snapshot);
    }

    public SForgeEntitySnapshot(SForgeEntity entity) {
        super(entity);
    }

    @Override
    public SForgeEntity spawnEntity() {
        org.spongepowered.api.entity.Entity entity = ((SExactPosition)this.position).getSpongeLocation().createEntity(this.spongeType);
        SForgeEntity sEntity = new SForgeEntity(entity);
        applyDefault(sEntity);
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
