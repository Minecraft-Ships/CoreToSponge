package org.ships.implementation.sponge.events.events.entity.interact;

import org.core.entity.Entity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.world.direction.Direction;
import org.core.world.position.BlockPosition;
import org.core.world.position.Position;

public class SEntityInteractEvent <T extends Entity> implements EntityInteractEvent<T> {

    protected boolean cancelled;
    protected Position<? extends Number> interactPoint;
    protected T entity;
    protected int click;

    public SEntityInteractEvent(Position<? extends Number> position, int click, T entity){
        this.entity = entity;
        this.interactPoint = position;
        this.click = click;
    }

    @Override
    public Position<? extends Number> getInteractPosition() {
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

    public static class SPlayerInteractWithBlockEvent extends SEntityInteractEvent<LivePlayer> implements EntityInteractEvent.WithBlock.AsPlayer{

        protected Direction clickedSide;

        public SPlayerInteractWithBlockEvent(BlockPosition position, int click, Direction clickedSide, LivePlayer entity) {
            super(position, click, entity);
            this.clickedSide = clickedSide;
        }

        @Override
        public BlockPosition getInteractPosition(){
            return (BlockPosition)super.getInteractPosition();
        }

        @Override
        public Direction getClickedBlockFace() {
            return this.clickedSide;
        }
    }
}
