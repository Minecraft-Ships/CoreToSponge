package org.core.implementation.sponge.entity.forge.live;

import org.core.entity.EntitySnapshot;
import org.core.implementation.sponge.entity.SEntityType;
import org.core.implementation.sponge.entity.SLiveEntity;
import org.core.implementation.sponge.entity.forge.snapshot.SForgeEntitySnapshot;

public class SForgeEntity extends SLiveEntity {

    public SForgeEntity(org.spongepowered.api.entity.Entity entity) {
        super(entity);
    }

    @Override
    public SEntityType<SForgeEntity, SForgeEntitySnapshot> getType() {
        return new SEntityType.SForgedEntityType(this.entity.type());
    }

    @Override
    public EntitySnapshot<?> createSnapshot() {
        return new SForgeEntitySnapshot(this);
    }
}
