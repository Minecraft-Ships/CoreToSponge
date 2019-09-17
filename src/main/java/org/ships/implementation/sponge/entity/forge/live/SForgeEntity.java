package org.ships.implementation.sponge.entity.forge.live;

import org.core.entity.EntitySnapshot;
import org.ships.implementation.sponge.entity.SEntityType;
import org.ships.implementation.sponge.entity.SLiveEntity;
import org.ships.implementation.sponge.entity.forge.snapshot.SForgeEntitySnapshot;

public class SForgeEntity extends SLiveEntity {

    public SForgeEntity(org.spongepowered.api.entity.Entity entity) {
        super(entity);
    }

    @Override
    public SEntityType<SForgeEntity, SForgeEntitySnapshot> getType() {
        return new SEntityType.SForgedEntityType(this.entity.getType());
    }

    @Override
    public EntitySnapshot createSnapshot() {
        return new SForgeEntitySnapshot(this);
    }
}
