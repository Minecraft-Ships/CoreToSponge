package org.ships.implementation.sponge.events;

import net.kyori.adventure.text.Component;
import org.array.utils.ArrayUtils;
import org.core.CorePlugin;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.Event;
import org.core.event.HEvent;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.world.direction.Direction;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.events.events.block.tileentity.SSignChangeEvent;
import org.ships.implementation.sponge.events.events.entity.interact.SEntityInteractEvent;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.utils.DirectionUtils;
import org.ships.implementation.sponge.world.position.block.entity.sign.SSignTileEntitySnapshot;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.value.ListValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.math.vector.Vector3i;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpongeListener {

    public static <E extends Event> E call(E event) {
        List<SEventLaunch> methods = new ArrayList<>(getMethods(event.getClass()));
        for (SEventLaunch method : methods) {
            method.run(event);
        }
        return event;
    }

    private static Set<SEventLaunch> getMethods(Class<? extends Event> classEvent) {
        Set<SEventLaunch> methods = new HashSet<>();
        CorePlugin.getEventManager().getEventListeners().forEach((key, value) -> value.forEach(el -> {
            for (Method method : el.getClass().getDeclaredMethods()) {
                if (method.getDeclaredAnnotationsByType(HEvent.class) == null) {
                    continue;
                }
                if (methods.stream().anyMatch(m -> method.getName().contains("lambda") || method.getName().contains("$"))) {
                    continue;
                }
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 0) {
                    System.err.println("Failed to know what to do: HEvent found on method, but no event on " + el.getClass().getName() + "." + method.getName() + "()");
                    continue;
                }
                if (!Modifier.isPublic(method.getModifiers())) {
                    continue;
                }
                Class<?> class1 = parameters[0].getType();
                if (!Event.class.isAssignableFrom(classEvent)) {
                    System.err.println("Failed to know what to do: HEvent found on method, but no known event on " + el.getClass().getName() + "." + method.getName() + "(" + ArrayUtils.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), parameters) + ")");
                }
                if (class1.isAssignableFrom(classEvent)) {
                    methods.add(new SEventLaunch(key, el, method));
                }
            }
        }));
        return methods;
    }

    @org.spongepowered.api.event.Listener
    public void onSignChangeEvent(org.spongepowered.api.event.block.entity.ChangeSignEvent event) {
        SSignChangeEvent sEvent;
        Object rootCause = event.cause().root();
        SyncBlockPosition bp = new SBlockPosition(event.sign().location());
        SignTileEntitySnapshot from = new SSignTileEntitySnapshot(event.text().asImmutable());
        SignTileEntitySnapshot to = new SSignTileEntitySnapshot(event.text());
        if (rootCause instanceof org.spongepowered.api.entity.living.player.Player) {
            LivePlayer player = (LivePlayer) ((SpongePlatform) CorePlugin.getPlatform()).createEntityInstance((org.spongepowered.api.entity.living.player.Player) rootCause);
            sEvent = new SSignChangeEvent.SSignChangeEventByPlayer(bp, from, to, player);
        } else {
            sEvent = new SSignChangeEvent(bp, from, to);
        }
        call(sEvent);
        if (sEvent.isCancelled()) {
            event.setCancelled(true);
        }
        ListValue.Mutable<Component> data = event.text();
        List<Component> text = sEvent.getTo().getText().stream().map(t -> ((AdventureText) t).getComponent()).collect(Collectors.toList());
        data.set(text);
    }

    @Listener
    public void onPlayerPrimaryInteractWithBlock(InteractBlockEvent.Primary.Start event, @Root Player player) {
        this.onPlayerInteractWithBlock(event, player);
    }

    @Listener
    public void onPlayerSecondaryInteractWithBlock(InteractBlockEvent.Secondary event, @Root Player player) {
        this.onPlayerInteractWithBlock(event, player);
    }

    private <E extends InteractBlockEvent & Cancellable> void onPlayerInteractWithBlock(org.spongepowered.api.event.block.InteractBlockEvent event, org.spongepowered.api.entity.living.player.Player player) {
        LivePlayer player1 = new SLivePlayer(player);
        BlockSnapshot snapshot = event.block();

        SyncBlockPosition bp = player1.getPosition().getWorld().getPosition(snapshot.position().x(), snapshot.position().y(), snapshot.position().z());
        Vector3i spongeVector = event.targetSide().asBlockOffset();
        int action = -1;
        if (event instanceof org.spongepowered.api.event.block.InteractBlockEvent.Primary) {
            action = EntityInteractEvent.PRIMARY_CLICK_ACTION;
        } else if (event instanceof org.spongepowered.api.event.block.InteractBlockEvent.Secondary) {
            action = EntityInteractEvent.SECONDARY_CLICK_ACTION;
        }
        Direction direction = DirectionUtils.getCoreDirection(event.targetSide());
        SEntityInteractEvent.SPlayerInteractWithBlockEvent event1 = new SEntityInteractEvent.SPlayerInteractWithBlockEvent(bp, action, direction, player1);
        call(event1);
        if (event1.isCancelled()) {
            ((Cancellable) event).setCancelled(true);
        }
    }
}
