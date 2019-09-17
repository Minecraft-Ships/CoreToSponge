package org.ships.implementation.sponge.entity;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.ships.implementation.sponge.entity.forge.live.SForgeEntity;
import org.ships.implementation.sponge.entity.forge.snapshot.SForgeEntitySnapshot;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.entity.living.human.player.snapshot.SPlayerSnapshot;

public abstract class SEntityType<T extends LiveEntity, S extends EntitySnapshot<T>> implements EntityType<T, S> {

    protected org.spongepowered.api.entity.EntityType type;

    public SEntityType(org.spongepowered.api.entity.EntityType type){
        this.type = type;
    }

    public org.spongepowered.api.entity.EntityType getSpongeType(){
        return this.type;
    }

    @Override
    public String getId() {
        return this.type.getId();
    }

    @Override
    public String getName() {
        return this.type.getName();
    }

    public static class SPlayerType extends SEntityType<LivePlayer, PlayerSnapshot> {

        public SPlayerType() {
            super(org.spongepowered.api.entity.EntityTypes.PLAYER);
        }

        @Override
        public Class<SLivePlayer> getEntityClass() {
            return SLivePlayer.class;
        }

        @Override
        public Class<SPlayerSnapshot> getSnapshotClass() {
            return SPlayerSnapshot.class;
        }
    }

    public static class SForgedEntityType extends SEntityType<SForgeEntity, SForgeEntitySnapshot> {

        public SForgedEntityType(org.spongepowered.api.entity.EntityType type) {
            super(type);
        }

        @Override
        public Class getEntityClass() {
            return SForgeEntity.class;
        }

        @Override
        public Class getSnapshotClass() {
            return SForgeEntitySnapshot.class;
        }
    }
}
