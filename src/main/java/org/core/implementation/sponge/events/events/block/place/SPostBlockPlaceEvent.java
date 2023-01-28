package org.core.implementation.sponge.events.events.block.place;

import org.core.event.events.block.BlockChangeEvent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Collection;

public class SPostBlockPlaceEvent extends SAbstractBlockPlaceEvent implements BlockChangeEvent.Place.Post {

    public SPostBlockPlaceEvent(SyncBlockPosition position,
                                BlockDetails before,
                                BlockDetails after,
                                Collection<BlockSnapshot<SyncBlockPosition>> collection) {
        super(position, before, after, collection);
    }
}
