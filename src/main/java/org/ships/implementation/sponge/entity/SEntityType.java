package org.ships.implementation.sponge.entity;

import org.core.entity.EntityType;

public abstract class SEntityType implements EntityType {

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

    public static class SUnknownEntityType extends SEntityType {

        public SUnknownEntityType(org.spongepowered.api.entity.EntityType type) {
            super(type);
        }

        @Override
        public Class getEntityClass() {
            return null;
        }

        @Override
        public Class getSnapshotClass() {
            return null;
        }
    }
}
