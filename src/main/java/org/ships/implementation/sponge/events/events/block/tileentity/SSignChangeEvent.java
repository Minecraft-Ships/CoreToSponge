package org.ships.implementation.sponge.events.events.block.tileentity;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.block.tileentity.SignChangeEvent;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;

public class SSignChangeEvent implements SignChangeEvent {

    protected SignTileEntitySnapshot to;
    protected SignTileEntitySnapshot from;
    protected SyncBlockPosition bp;
    protected boolean cancelled;

    public SSignChangeEvent(SyncBlockPosition position, SignTileEntitySnapshot from, SignTileEntitySnapshot to){
        this.to = to;
        this.from = from;
        this.bp = position;
    }

    @Override
    public SignTileEntitySnapshot getTo() {
        return this.to;
    }

    @Override
    public SignChangeEvent setTo(SignTileEntitySnapshot snapshot) {
        this.from = snapshot;
        return this;
    }

    @Override
    public SignTileEntitySnapshot getFrom() {
        return this.from;
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
    public SyncBlockPosition getPosition() {
        return this.bp;
    }

    public static class SSignChangeEventByPlayer extends SSignChangeEvent implements SignChangeEvent.ByPlayer{

        protected LivePlayer player;

        public SSignChangeEventByPlayer(SyncBlockPosition position, SignTileEntitySnapshot from, SignTileEntitySnapshot to, LivePlayer player) {
            super(position, from, to);
            this.player = player;
        }

        @Override
        public LivePlayer getEntity() {
            return this.player;
        }
    }
}
