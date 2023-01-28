package org.core.implementation.sponge.events;

import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.adventureText.format.NamedTextColours;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.Event;
import org.core.event.HEvent;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.implementation.sponge.events.events.block.place.SPlayerBlockPlaceEvent;
import org.core.implementation.sponge.events.events.block.place.SPostBlockPlaceEvent;
import org.core.implementation.sponge.events.events.block.tileentity.SSignChangeEvent;
import org.core.implementation.sponge.events.events.entity.command.SEntityCommandEvent;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.implementation.sponge.world.position.block.details.blocks.snapshot.SBlockSnapshot;
import org.core.implementation.sponge.world.position.block.entity.sign.SSignTileEntitySnapshot;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.transaction.Operations;
import org.spongepowered.api.data.value.ListValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.server.ServerLocation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpongeListener {

    public static <E extends Event> E call(E event) {
        getMethods(event.getClass()).forEach(method -> method.run(event));
        return event;
    }

    private static Set<SEventLaunch> getMethods(Class<? extends Event> classEvent) {
        Set<SEventLaunch> methods = new HashSet<>();
        TranslateCore.getEventManager().getEventListeners().forEach((key, value) -> value.forEach(el -> {
            for (Method method : el.getClass().getDeclaredMethods()) {
                if (method.getDeclaredAnnotationsByType(HEvent.class) == null) {
                    continue;
                }
                if (method.getName().contains("lambda") || method.getName().contains("$")) {
                    continue;
                }
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 0) {
                    TranslateCore
                            .getConsole()
                            .sendMessage(AText
                                                 .ofPlain("Failed to know what to do: HEvent found on "
                                                                  + "method, but no event on " + el.getClass().getName()
                                                                  + "." + method.getName() + "()")
                                                 .withColour(NamedTextColours.RED));
                    continue;
                }
                if (!Modifier.isPublic(method.getModifiers())) {
                    continue;
                }
                Class<?> class1 = parameters[0].getType();
                if (!Event.class.isAssignableFrom(classEvent)) {
                    TranslateCore
                            .getConsole()
                            .sendMessage(AText
                                                 .ofPlain(
                                                         "Failed to know what to do: HEvent found on method, but no known "
                                                                 + "event on " + el.getClass().getName() + "."
                                                                 + method.getName() + "(" + Arrays
                                                                 .stream(parameters)
                                                                 .map(p -> p.getType().getSimpleName() + " "
                                                                         + p.getName())
                                                                 .collect(Collectors.joining(", ")))
                                                 .withColour(NamedTextColours.RED));
                }
                if (class1.isAssignableFrom(classEvent)) {
                    methods.add(new SEventLaunch(key, el, method));
                }
            }
        }));
        return methods;
    }

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
            call(e);
        });
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
            call(e);
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
        call(e);
    }

    @org.spongepowered.api.event.Listener
    public void onSignChangeEvent(org.spongepowered.api.event.block.entity.ChangeSignEvent event) {
        SSignChangeEvent sEvent;
        Object rootCause = event.cause().root();
        SyncBlockPosition bp = new SBlockPosition(event.sign().location());
        SignTileEntitySnapshot from = new SSignTileEntitySnapshot(event.text().asImmutable());
        SignTileEntitySnapshot to = new SSignTileEntitySnapshot(event.text());
        if (rootCause instanceof org.spongepowered.api.entity.living.player.Player) {
            LivePlayer player = (LivePlayer) ((SpongePlatform) TranslateCore.getPlatform()).createEntityInstance(
                    (Entity) rootCause);
            sEvent = new SSignChangeEvent.SSignChangeEventByPlayer(bp, from, to, player);
        } else {
            sEvent = new SSignChangeEvent(bp, from, to);
        }
        call(sEvent);
        if (sEvent.isCancelled()) {
            event.setCancelled(true);
        }
        ListValue.Mutable<Component> data = event.text();
        List<Component> text = sEvent
                .getTo()
                .getText()
                .stream()
                .map(t -> ((AdventureText) t).getComponent())
                .collect(Collectors.toList());
        data.set(text);
    }
}
