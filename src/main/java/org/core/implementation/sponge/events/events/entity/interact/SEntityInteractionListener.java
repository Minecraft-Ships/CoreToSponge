package org.core.implementation.sponge.events.events.entity.interact;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.implementation.sponge.events.SEventManager;
import org.core.implementation.sponge.events.events.entity.interact.block.SPlayerInteractBlockEvent;
import org.core.implementation.sponge.utils.DirectionUtils;
import org.core.world.direction.Direction;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.stream.Stream;

public class SEntityInteractionListener {

    @Listener
    public void onPlayerPrimaryInteractWithBlock(InteractBlockEvent.Primary.Start event, @Root Player player) {
        GameMode playerGamemode = player
                .get(Keys.GAME_MODE)
                .orElseThrow(() -> new RuntimeException("Player must have a gamemode"));
        ItemStackSnapshot handItem = event
                .context()
                .get(EventContextKeys.USED_ITEM)
                .orElseGet(ItemStackSnapshot::empty);
        if (playerGamemode.equals(GameModes.CREATIVE.get())) {
            //need tag of sword ideally
            boolean willBreak = Stream
                    .of(ItemTypes.DIAMOND_SWORD, ItemTypes.STONE_SWORD, ItemTypes.GOLDEN_SWORD, ItemTypes.IRON_SWORD,
                        ItemTypes.WOODEN_SWORD)
                    .noneMatch(type -> type.get().equals(handItem.type()));
            if (willBreak) {
                return;
            }

        }
        this.onPlayerInteractWithBlock(event, player);
    }

    @Listener
    public void onPlayerSecondaryInteractWithBlock(InteractBlockEvent.Secondary event, @Root Player player) {
        this.onPlayerInteractWithBlock(event, player);
    }

    private void onPlayerInteractWithBlock(org.spongepowered.api.event.block.InteractBlockEvent event,
                                           org.spongepowered.api.entity.living.player.Player player) {
        LivePlayer player1 = new SLivePlayer(player);
        BlockSnapshot snapshot = event.block();

        SyncBlockPosition bp = player1
                .getPosition()
                .getWorld()
                .getPosition(snapshot.position().x(), snapshot.position().y(), snapshot.position().z());
        int action = -1;
        if (event instanceof org.spongepowered.api.event.block.InteractBlockEvent.Primary) {
            action = EntityInteractEvent.PRIMARY_CLICK_ACTION;
        } else if (event instanceof org.spongepowered.api.event.block.InteractBlockEvent.Secondary) {
            action = EntityInteractEvent.SECONDARY_CLICK_ACTION;
        }
        Direction direction = DirectionUtils.getCoreDirection(event.targetSide());
        SPlayerInteractBlockEvent event1 = new SPlayerInteractBlockEvent(bp, action, direction, player1);
        SEventManager.call(event1);
        if (event1.isCancelled()) {
            ((Cancellable) event).setCancelled(true);
        }
    }
}
