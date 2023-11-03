package org.core.implementation.sponge.events.events.entity.move;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.entity.EntityMoveEvent;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;

public class SPlayerMoveEvent extends SEntityMoveEvent<LivePlayer> implements EntityMoveEvent.AsPlayer {

    private final MoveReason reason;

    public SPlayerMoveEvent(SyncExactPosition before, SyncExactPosition after, LivePlayer entity, MoveReason reason) {
        super(before, after, entity);
        this.reason = reason;
    }

    @Override
    public @NotNull MoveReason getReason() {
        return this.reason;
    }
}
