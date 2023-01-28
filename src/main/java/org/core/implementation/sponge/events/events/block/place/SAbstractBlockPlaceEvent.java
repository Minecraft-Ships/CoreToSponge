package org.core.implementation.sponge.events.events.block.place;

import org.core.event.events.block.BlockChangeEvent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Collection;
import java.util.Collections;

public abstract class SAbstractBlockPlaceEvent implements BlockChangeEvent.Place {

    private final SyncBlockPosition position;
    private final BlockDetails before;
    private final BlockDetails after;
    private final Collection<BlockSnapshot<SyncBlockPosition>> collection;

    public SAbstractBlockPlaceEvent(SyncBlockPosition position,
                                    BlockDetails before,
                                    BlockDetails after,
                                    Collection<BlockSnapshot<SyncBlockPosition>> collection) {
        this.before = before;
        this.after = after;
        this.position = position;
        this.collection = collection;
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
    public Collection<BlockSnapshot<SyncBlockPosition>> getAffected() {
        return Collections.unmodifiableCollection(this.collection);
    }

    @Override
    public SyncBlockPosition getPosition() {
        return this.position;
    }
}
