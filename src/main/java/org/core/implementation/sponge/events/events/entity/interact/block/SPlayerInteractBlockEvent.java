package org.core.implementation.sponge.events.events.entity.interact.block;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.world.direction.Direction;
import org.core.world.position.impl.sync.SyncBlockPosition;

public class SPlayerInteractBlockEvent extends SEntityInteractBlockEvent<LivePlayer>
        implements EntityInteractEvent.WithBlock.AsPlayer {

    private final Direction clickedSide;

    public SPlayerInteractBlockEvent(SyncBlockPosition position, int click, Direction clickedSide, LivePlayer entity) {
        super(position, click, entity);
        this.clickedSide = clickedSide;
    }

    @Override
    public Direction getClickedBlockFace() {
        return this.clickedSide;
    }
}
