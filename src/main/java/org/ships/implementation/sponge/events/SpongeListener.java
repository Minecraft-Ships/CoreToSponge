package org.ships.implementation.sponge.events;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.core.CorePlugin;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.Event;
import org.core.event.HEvent;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.world.direction.Direction;
import org.core.world.position.BlockPosition;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.events.events.block.tileentity.SSignChangeEvent;
import org.ships.implementation.sponge.events.events.entity.interact.SEntityInteractEvent;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.ships.implementation.sponge.utils.DirectionUtils;
import org.ships.implementation.sponge.world.position.SBlockPosition;
import org.ships.implementation.sponge.world.position.block.entity.sign.SSignTileEntitySnapshot;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

public class SpongeListener {

    @org.spongepowered.api.event.Listener
    public void onSignChangeEvent(org.spongepowered.api.event.block.tileentity.ChangeSignEvent event){
        SSignChangeEvent sEvent;
        Object rootCause = event.getCause().root();
        BlockPosition bp = new SBlockPosition(event.getTargetTile().getLocation());
        SignTileEntitySnapshot from = new SSignTileEntitySnapshot(event.getOriginalText());
        SignTileEntitySnapshot to = new SSignTileEntitySnapshot(event.getText());
        if (rootCause instanceof org.spongepowered.api.entity.living.player.Player) {
            LivePlayer player = (LivePlayer) ((SpongePlatform)CorePlugin.getPlatform()).createEntityInstance((org.spongepowered.api.entity.living.player.Player)rootCause);
            sEvent = new SSignChangeEvent.SSignChangeEventByPlayer(bp, from, to, player);
        }else{
            sEvent = new SSignChangeEvent(bp, from, to);
        }
        call(sEvent);
        if(sEvent.isCancelled()){
            event.setCancelled(true);
        }
        SignData data = event.getText();
        org.core.text.Text[] setToLines = sEvent.getTo().getLines();
        for(int A = 0; A < setToLines.length; A++){
            data.addElement(A, ((SText)setToLines[A]).toSponge());
            CorePlugin.getConsole().sendMessage(setToLines[A]);
        }
    }

    @org.spongepowered.api.event.Listener
    public void OnPlayerInteractWithBlock(org.spongepowered.api.event.block.InteractBlockEvent event, @org.spongepowered.api.event.filter.cause.Root org.spongepowered.api.entity.living.player.Player player){
        LivePlayer player1 = new SLivePlayer(player);
        Optional<Vector3d> opVector = event.getInteractionPoint();
        if(!opVector.isPresent()){
            return;
        }
        Vector3d vector = opVector.get();
        BlockPosition bp = player1.getPosition().getWorld().getPosition(vector.getX(), vector.getY(), vector.getZ()).toBlockPosition();
        Vector3i spongeVector = event.getTargetSide().asBlockOffset();
        int action = -1;
        if(event instanceof org.spongepowered.api.event.block.InteractBlockEvent.Primary){
            action = EntityInteractEvent.PRIMARY_CLICK_ACTION;
        }else if(event instanceof org.spongepowered.api.event.block.InteractBlockEvent.Secondary){
            action = EntityInteractEvent.SECONDARY_CLICK_ACTION;
        }
        Direction direction = DirectionUtils.getCoreDirection(event.getTargetSide());
        SEntityInteractEvent.SPlayerInteractWithBlockEvent event1 = new SEntityInteractEvent.SPlayerInteractWithBlockEvent(bp, action, direction, player1);
        call(event1);
        if(event1.isCancelled()){
            event.setCancelled(true);
        }
    }

    public static <E extends Event> E call(E event){
        List<SEventLaunch> methods = new ArrayList(getMethods(event.getClass()));
        for(int A = 0; A < methods.size(); A++){
            methods.get(A).run(event);
        }
        return event;
    }

    private static Set<SEventLaunch> getMethods(Class<? extends Event> classEvent){
        Set<SEventLaunch> methods = new HashSet<>();
        CorePlugin.getEventManager().getEventListeners().forEach((key, value) -> value.forEach(el -> {
            for (Method method : el.getClass().getDeclaredMethods()){
                if (method.getDeclaredAnnotationsByType(HEvent.class) == null){
                    continue;
                }
                if (methods.stream().anyMatch(m -> method.getName().contains("$"))){
                    continue;
                }
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 0){
                    System.err.println("Failed to know what to do: HEvent found on method, but no event on " + el.getClass().getName() + "." + method.getName() + "()");
                    continue;
                }
                if (!Modifier.isPublic(method.getModifiers())){
                    continue;
                }
                Class<?> class1 = parameters[0].getType();
                if (!Event.class.isAssignableFrom(classEvent)){
                    System.err.println("Failed to know what to do: HEvent found on method, but no known event on " + el.getClass().getName() + "." + method.getName() + "(" + CorePlugin.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), parameters) + ")");
                }
                if (class1.isAssignableFrom(classEvent)){
                    methods.add(new SEventLaunch(key, el, method));
                }
            }
        }));
        return methods;
    }
}
