package org.core.implementation.sponge.events.events.entity.damage;

import org.core.entity.LiveEntity;
import org.core.event.events.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

public class SEntityDeathEvent<E extends LiveEntity> implements EntityDeathEvent<E> {

    private E entity;
    private boolean isCancelled;

    public SEntityDeathEvent(@NotNull E entity) {
        this.entity = entity;
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
}
