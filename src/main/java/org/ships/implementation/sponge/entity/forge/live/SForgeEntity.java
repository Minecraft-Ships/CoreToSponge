package org.ships.implementation.sponge.entity.forge.live;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.ships.implementation.sponge.entity.SEntityType;
import org.ships.implementation.sponge.entity.SLiveEntity;

public class SForgeEntity extends SLiveEntity {

    public SForgeEntity(org.spongepowered.api.entity.Entity entity) {
        super(entity);
    }

    @Override
    public <E extends LiveEntity> EntityType<E, ? extends EntitySnapshot<E>> getType() {
        return new SEntityType.SForgedEntityType(this.entity.getType());
    }

    @Override
    public EntitySnapshot createSnapshot() {
        return null;
    }
}
