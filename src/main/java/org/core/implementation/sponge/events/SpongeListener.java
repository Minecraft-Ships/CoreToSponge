package org.core.implementation.sponge.events;

import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.EventPriority;
import org.core.event.events.entity.EntityDeathEvent;
import org.core.event.events.entity.EntityMoveEvent;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.implementation.sponge.events.events.block.place.SPlayerBlockPlaceEvent;
import org.core.implementation.sponge.events.events.block.place.SPostBlockPlaceEvent;
import org.core.implementation.sponge.events.events.block.tileentity.SSignChangeEvent;
import org.core.implementation.sponge.events.events.entity.command.SEntityCommandEvent;
import org.core.implementation.sponge.events.events.entity.damage.SEntityDeathEvent;
import org.core.implementation.sponge.events.events.entity.move.SEntityMoveEvent;
import org.core.implementation.sponge.events.events.entity.move.SPlayerMoveEvent;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.implementation.sponge.world.position.block.details.blocks.snapshot.SBlockSnapshot;
import org.core.implementation.sponge.world.position.block.entity.sign.SSignSideSnapshot;
import org.core.implementation.sponge.world.position.block.entity.sign.SSignTileEntitySnapshot;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.implementation.sponge.world.position.synced.SExactPosition;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.transaction.Operations;
import org.spongepowered.api.data.value.ListValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.List;

public class SpongeListener {

    @org.spongepowered.api.event.Listener
    public void onPlaceEvent(ChangeBlockEvent.All event) {
        List<org.core.world.position.block.details.BlockSnapshot<SyncBlockPosition>> list = event
                .transactions()
                .stream()
                .map(transaction -> (org.core.world.position.block.details.BlockSnapshot<SyncBlockPosition>) new SBlockSnapshot.SSyncedBlockSnapshot(
                        transaction.finalReplacement()))
                .toList();
        event.transactions(Operations.PLACE.get()).forEach(transaction -> {
            BlockSnapshot original = transaction.original();
            BlockSnapshot last = transaction.finalReplacement();
            ServerLocation loc = event.world().location(original.position());

            SBlockSnapshot.SSyncedBlockSnapshot originalSnapshot = new SBlockSnapshot.SSyncedBlockSnapshot(original);
            SBlockSnapshot.SSyncedBlockSnapshot lastSnapshot = new SBlockSnapshot.SSyncedBlockSnapshot(last);
            SBlockPosition position = new SBlockPosition(loc);

            SPostBlockPlaceEvent e = new SPostBlockPlaceEvent(position, originalSnapshot, lastSnapshot, list);
            SEventManager.call(e);
        });
    }

    @org.spongepowered.api.event.Listener
    public void onEntityMove(MoveEntityEvent event) {
        World<?, ?> world = event.entity().world();
        SyncExactPosition before = new SExactPosition(world.location(event.originalPosition()));
        SyncExactPosition after = new SExactPosition(world.location(event.destinationPosition()));
        LiveEntity entity = ((SpongePlatform) TranslateCore.getPlatform()).createEntityInstance(event.entity());

        EntityMoveEvent<?> coreEvent;
        if (entity instanceof LivePlayer player) {
            coreEvent = new SPlayerMoveEvent(before, after, player, EntityMoveEvent.AsPlayer.MoveReason.NATURAL);
        } else {
            coreEvent = new SEntityMoveEvent<>(before, after, entity);
        }
        SEventManager.call(coreEvent);
        event.setCancelled(coreEvent.isCancelled());
    }

    @org.spongepowered.api.event.Listener
    public void onEntityMove(DamageEntityEvent event) {
        if (!event.willCauseDeath()) {
            return;
        }
        LiveEntity entity = ((SpongePlatform) TranslateCore.getPlatform()).createEntityInstance(event.entity());

        EntityDeathEvent<?> coreEvent = new SEntityDeathEvent<>(entity);
        SEventManager.call(coreEvent);
        event.setCancelled(coreEvent.isCancelled());
    }

    @org.spongepowered.api.event.Listener
    public void onPlaceEvent(ChangeBlockEvent.All event, @First Player player) {
        LivePlayer lPlayer = new SLivePlayer(player);
        List<org.core.world.position.block.details.BlockSnapshot<SyncBlockPosition>> list = event
                .transactions()
                .stream()
                .map(transaction -> (org.core.world.position.block.details.BlockSnapshot<SyncBlockPosition>) new SBlockSnapshot.SSyncedBlockSnapshot(
                        transaction.finalReplacement()))
                .toList();
        event.transactions(Operations.PLACE.get()).forEach(transaction -> {
            BlockSnapshot original = transaction.original();
            BlockSnapshot last = transaction.finalReplacement();
            ServerLocation loc = event.world().location(original.position());

            SBlockSnapshot.SSyncedBlockSnapshot originalSnapshot = new SBlockSnapshot.SSyncedBlockSnapshot(original);
            SBlockSnapshot.SSyncedBlockSnapshot lastSnapshot = new SBlockSnapshot.SSyncedBlockSnapshot(last);
            SBlockPosition position = new SBlockPosition(loc);

            SPlayerBlockPlaceEvent e = new SPlayerBlockPlaceEvent(position, originalSnapshot, lastSnapshot, lPlayer,
                                                                  list);
            SEventManager.call(e);
            if (e.isCancelled()) {
                transaction.invalidate();
            }
        });
    }

    @org.spongepowered.api.event.Listener
    public void onCommandEvent(org.spongepowered.api.event.command.ExecuteCommandEvent event) {
        if (!(event.commandCause().audience() instanceof Player sPlayer)) {
            return;
        }
        LivePlayer player = new SLivePlayer(sPlayer);
        SEntityCommandEvent e = new SEntityCommandEvent(player, event.originalCommand().split(" "));
        SEventManager.call(e);
    }

    @org.spongepowered.api.event.Listener
    public void onSignChangeEvent(org.spongepowered.api.event.block.entity.ChangeSignEvent event) {
        SSignChangeEvent sEvent;
        Object rootCause = event.cause().root();
        SyncBlockPosition bp = new SBlockPosition(event.sign().location());
        SignTileEntitySnapshot from = new SSignTileEntitySnapshot(event.text().asImmutable().get());
        SSignSideSnapshot to = new SSignTileEntitySnapshot().getSide(true);

        if (rootCause instanceof org.spongepowered.api.entity.living.player.Player) {
            LivePlayer player = (LivePlayer) ((SpongePlatform) TranslateCore.getPlatform()).createEntityInstance(
                    (Entity) rootCause);
            sEvent = new SSignChangeEvent.SSignChangeEventByPlayer(bp, from, to, player, false);
        } else {
            sEvent = new SSignChangeEvent(bp, from, to, false);
        }
        SEventManager.call(sEvent);
        if (sEvent.isCancelled()) {
            event.setCancelled(true);
        }
        ListValue.Mutable<Component> data = event.text();
        List<Component> text = to.getLines();
        data.set(text);
    }
}
