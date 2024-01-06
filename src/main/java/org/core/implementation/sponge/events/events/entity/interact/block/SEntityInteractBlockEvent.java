package org.core.implementation.sponge.events.events.entity.interact.block;

import org.core.entity.Entity;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.implementation.sponge.events.events.entity.interact.SEntityInteractEvent;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncPosition;

public class SEntityInteractBlockEvent<T extends Entity<?>> extends SEntityInteractEvent<T>
        implements EntityInteractEvent.WithBlock<T> {
    public SEntityInteractBlockEvent(SyncPosition<Integer> position, int click, T entity) {
        super(position, click, entity);
    }

    @Override
    public SyncBlockPosition getInteractPosition() {
        return (SyncBlockPosition) super.getInteractPosition();
    }


}
