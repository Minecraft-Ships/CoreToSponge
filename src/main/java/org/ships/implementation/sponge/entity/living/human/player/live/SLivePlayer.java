package org.ships.implementation.sponge.entity.living.human.player.live;

import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.human.AbstractHuman;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.Player;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.inventory.inventories.PlayerInventory;
import org.core.source.viewer.CommandViewer;
import org.ships.implementation.sponge.entity.SLiveEntity;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class SLivePlayer extends SLiveEntity implements LivePlayer {

    public SLivePlayer(org.spongepowered.api.entity.living.player.Player entity) {
        super(entity);
    }

    @Override
    public org.spongepowered.api.entity.living.player.Player getSpongeEntity(){
        return (org.spongepowered.api.entity.living.player.Player)super.getSpongeEntity();
    }

    @Override
    public boolean isViewingInventory() {
        return getSpongeEntity().isViewingInventory();
    }

    @Override
    public PlayerInventory getInventory() {
        return null;
    }

    @Override
    public int getFoodLevel() {
        return getSpongeEntity().get(Keys.FOOD_LEVEL).get();
    }

    @Override
    public double getExhaustionLevel() {
        return getSpongeEntity().get(Keys.EXHAUSTION).get();
    }

    @Override
    public double getSaturationLevel() {
        return getSpongeEntity().get(Keys.SATURATION).get();
    }

    @Override
    public AbstractHuman setFood(int value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.FOOD_LEVEL, value);
        return this;
    }

    @Override
    public AbstractHuman setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.EXHAUSTION, value);
        return this;
    }

    @Override
    public AbstractHuman setSaturationLevel(double value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.SATURATION, value);
        return this;
    }

    @Override
    public EntityType<Player, PlayerSnapshot> getType() {
        return EntityTypes.PLAYER;
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return null;
    }

    @Override
    public CommandViewer sendMessage(String message) {
        getSpongeEntity().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
        return this;
    }

    @Override
    public CommandViewer sendMessagePlain(String message) {
        getSpongeEntity().sendMessage(Text.of(TextSerializers.FORMATTING_CODE.stripCodes(message)));
        return this;
    }
}
