package org.core.implementation.sponge.events.events.block.place;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.block.BlockChangeEvent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Collection;

public class SPlayerBlockPlaceEvent extends SAbstractBlockPlaceEvent implements BlockChangeEvent.Place.ByPlayer {

    private boolean cancelled;
    private final LivePlayer player;

    public SPlayerBlockPlaceEvent(SyncBlockPosition position,
                                  BlockDetails before,
                                  BlockDetails after,
                                  LivePlayer player,
                                  Collection<BlockSnapshot<SyncBlockPosition>> collection) {
        super(position, before, after, collection);
        this.player = player;
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
    public LivePlayer getEntity() {
        return this.player;
    }
}
