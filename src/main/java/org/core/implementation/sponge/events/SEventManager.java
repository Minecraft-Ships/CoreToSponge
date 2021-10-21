package org.core.implementation.sponge.events;

import org.core.event.Event;
import org.core.event.EventListener;
import org.core.event.EventManager;
import org.core.platform.plugin.Plugin;

import java.util.*;

public class SEventManager implements EventManager {

    Map<Plugin, Set<EventListener>> eventListeners = new HashMap<>();
    SpongeListener listener = new SpongeListener();

    public SpongeListener getRawListener(){
        return this.listener;
    }

    @Override
    public <E extends Event> E callEvent(E event) {
        return this.listener.call(event);
    }

    @Override
    public EventManager register(Plugin plugin, EventListener... listeners) {
        Set<EventListener> listeners1 = this.eventListeners.get(plugin);
        boolean requiresPut = false;
        if(listeners1 == null){
            requiresPut = true;
            listeners1 = new HashSet<>();
        }
        listeners1.addAll(Arrays.asList(listeners));
        if(requiresPut){
            this.eventListeners.put(plugin, listeners1);
        }else{
            this.eventListeners.replace(plugin, listeners1);
        }
        return this;
    }

    @Override
    public Map<Plugin, Set<EventListener>> getEventListeners() {
        return this.eventListeners;
    }
}
