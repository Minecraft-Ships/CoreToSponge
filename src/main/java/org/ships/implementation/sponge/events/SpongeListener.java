package org.ships.implementation.sponge.events;

import org.core.CorePlugin;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.Event;
import org.core.event.HEvent;
import org.core.world.position.BlockPosition;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.ships.implementation.sponge.events.events.SSignChangeEvent;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.position.SBlockPosition;
import org.ships.implementation.sponge.world.position.block.entity.sign.SSignTileEntitySnapshot;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

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
        ListValue<Text> toLines = event.getText().lines();
        String[] setToLines = sEvent.getTo().getLines();
        for(int A = 0; A < setToLines.length; A++){
            toLines.set(A, TextSerializers.FORMATTING_CODE.deserialize(setToLines[A]));
        }

    }

    public static <E extends Event> E call(E event){
        Set<SEventLaunch> methods = getMethods(event.getClass());
        methods.stream().forEach(m -> m.run(event));
        return event;
    }

    private static Set<SEventLaunch> getMethods(Class<? extends Event> classEvent){
        Set<SEventLaunch> methods = new HashSet<>();
        CorePlugin.getEventManager().getEventListeners().entrySet().stream().forEach(e -> e.getValue().stream().forEach(el -> {
            for (Method method : el.getClass().getDeclaredMethods()){
                if(method.getDeclaredAnnotationsByType(HEvent.class) == null){
                    continue;
                }
                if(methods.stream().anyMatch(m -> method.getName().contains("$"))){
                    continue;
                }
                Parameter[] parameters = method.getParameters();
                if(parameters.length == 0){
                    System.err.println("Failed to know what to do: HEvent found on method, but no event on " + el.getClass().getName() + "." + method.getName() + "()");
                    continue;
                }
                if(!Modifier.isPublic(method.getModifiers())){
                    continue;
                }
                Class<? extends Object> class1 = parameters[0].getType();
                if(!Event.class.isAssignableFrom(classEvent)){
                    System.err.println("Failed to know what to do: HEvent found on method, but no known event on " + el.getClass().getName() + "." + method.getName() + "(" + CorePlugin.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), parameters) + ")");
                }
                if(class1.isAssignableFrom(classEvent)){
                    methods.add(new SEventLaunch(e.getKey(), el, method));
                }
            }
        }));
        return methods;
    }
}
