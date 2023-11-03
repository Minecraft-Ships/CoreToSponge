package org.core.implementation.sponge.events.events.entity.move;

import org.core.entity.LiveEntity;
import org.core.event.events.entity.EntityMoveEvent;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;

public class SEntityMoveEvent<E extends LiveEntity> implements EntityMoveEvent<E> {

    private boolean isCancelled;
    private final E entity;
    private final SyncExactPosition before;
    private final SyncExactPosition after;

    public SEntityMoveEvent(SyncExactPosition before, SyncExactPosition after, E entity) {
        this.entity = entity;
        this.before = before;
        this.after = after;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        this.isCancelled = value;
    }

    @Override
    public E getEntity() {
        return this.entity;
    }

    @Override
    public @NotNull SyncExactPosition getBeforePosition() {
        return this.before;
    }

    @Override
    public @NotNull SyncExactPosition getAfterPosition() {
        return this.after;
    }
}
