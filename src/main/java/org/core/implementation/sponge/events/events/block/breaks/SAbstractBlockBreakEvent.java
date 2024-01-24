package org.core.implementation.sponge.events.events.block.breaks;

import org.core.event.events.block.BlockChangeEvent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.impl.sync.SyncBlockPosition;

public abstract class SAbstractBlockBreakEvent implements BlockChangeEvent.Break {

    private final SyncBlockPosition position;
    private final BlockDetails before;
    private final BlockDetails after;

    public SAbstractBlockBreakEvent(SyncBlockPosition position, BlockDetails before, BlockDetails after) {
        this.before = before;
        this.after = after;
        this.position = position;
    }


    @Override
    public BlockDetails getBeforeState() {
        return this.before;
    }

    @Override
    public BlockDetails getAfterState() {
        return this.after;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return this.position;
    }
}
