package org.core.implementation.sponge.events.events.entity.interact;

import org.core.entity.Entity;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.world.position.impl.sync.SyncPosition;

public class SEntityInteractEvent<T extends Entity<?>> implements EntityInteractEvent<T> {

    protected final SyncPosition<? extends Number> interactPoint;
    protected final T entity;
    protected final int click;
    protected boolean cancelled;

    public SEntityInteractEvent(SyncPosition<? extends Number> position, int click, T entity) {
        this.entity = entity;
        this.interactPoint = position;
        this.click = click;
    }

    @Override
    public SyncPosition<? extends Number> getInteractPosition() {
        return this.interactPoint;
    }

    @Override
    public int getClickAction() {
        return this.click;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        this.cancelled = value;
    }

    @Override
    public T getEntity() {
        return this.entity;
    }
}
