package org.core.implementation.sponge.events.events.block.breaks;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.block.BlockChangeEvent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.impl.sync.SyncBlockPosition;

public class SPlayerBlockBreakEvent
        extends org.core.implementation.sponge.events.events.block.breaks.SAbstractBlockBreakEvent
        implements BlockChangeEvent.Break.Pre.ByPlayer {

    private final LivePlayer player;
    private boolean cancelled;

    public SPlayerBlockBreakEvent(SyncBlockPosition position,
                                  BlockDetails before,
                                  BlockDetails after,
                                  LivePlayer player) {
        super(position, before, after);
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
