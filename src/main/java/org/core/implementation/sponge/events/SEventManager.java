package org.core.implementation.sponge.events;

import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.format.NamedTextColours;
import org.core.event.Event;
import org.core.event.EventListener;
import org.core.event.EventManager;
import org.core.event.HEvent;
import org.core.implementation.sponge.events.events.entity.interact.SEntityInteractionListener;
import org.core.platform.plugin.Plugin;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class SEventManager implements EventManager {

    private final Map<Plugin, Set<EventListener>> eventListeners = new HashMap<>();
    private final SpongeListener listener = new SpongeListener();
    private final SEntityInteractionListener entityInteractionListener = new SEntityInteractionListener();


    public SpongeListener getRawGeneralListener() {
        return this.listener;
    }

    public SEntityInteractionListener getRawEntityInteractionListener() {
        return this.entityInteractionListener;
    }

    @Override
    public <E extends Event> E callEvent(E event) {
        return call(event);
    }

    @Override
    public EventManager register(Plugin plugin, EventListener... listeners) {
        Set<EventListener> listeners1 = this.eventListeners.get(plugin);
        boolean requiresPut = false;
        if (listeners1 == null) {
            requiresPut = true;
            listeners1 = new HashSet<>();
        }
        listeners1.addAll(Arrays.asList(listeners));
        if (requiresPut) {
            this.eventListeners.put(plugin, listeners1);
        } else {
            this.eventListeners.replace(plugin, listeners1);
        }
        return this;
    }

    @Override
    public Map<Plugin, Set<EventListener>> getEventListeners() {
        return this.eventListeners;
    }

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
}
