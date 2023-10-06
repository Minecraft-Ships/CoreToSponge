package org.core.implementation.sponge.events.events.block.tileentity;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.block.tileentity.SignChangeEvent;
import org.core.implementation.sponge.world.position.block.entity.sign.SSignSideSnapshot;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;

public class SSignChangeEvent implements SignChangeEvent {

    public static class SSignChangeEventByPlayer extends SSignChangeEvent implements SignChangeEvent.ByPlayer {

        protected LivePlayer player;

        public SSignChangeEventByPlayer(SyncBlockPosition position,
                                        SignTileEntitySnapshot from,
                                        SSignSideSnapshot to,
                                        LivePlayer player,
                                        boolean isEdit) {
            super(position, from, to, isEdit);
            this.player = player;
        }

        @Override
        public LivePlayer getEntity() {
            return this.player;
        }
    }

    protected final SyncBlockPosition bp;
    protected final SignTileEntitySnapshot from;
    private final SSignSideSnapshot to;
    private final boolean isEdit;
    protected boolean cancelled;

    public SSignChangeEvent(SyncBlockPosition position,
                            SignTileEntitySnapshot from,
                            SSignSideSnapshot to,
                            boolean isEdit) {
        this.to = to;
        this.from = from;
        this.bp = position;
        this.isEdit = isEdit;
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

    @Override
    public SignTileEntitySnapshot getSign() {
        return this.from;
    }

    @Override
    public SignSide getPreviousSide() {
        return this.from.getSide(this.to.isFront());
    }

    @Override
    public SignSide getChangingSide() {
        return this.to;
    }

    @Override
    public boolean isEdit() {
        return this.isEdit;
    }
}
