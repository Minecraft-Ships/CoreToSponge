package org.ships.implementation.sponge.events;

import org.core.CorePlugin;
import org.core.event.Event;
import org.core.event.EventListener;
import org.core.platform.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SEventLaunch {

    protected Plugin plugin;
    protected EventListener listener;
    protected Method method;

    public SEventLaunch(Plugin plugin, EventListener listener, Method method){
        this.plugin = plugin;
        this.listener = listener;
        this.method = method;
    }

    public void run(Event event){
        try {
            this.method.invoke(listener, event);
        } catch (IllegalAccessException e) {
            System.err.println("Failed to know what to do: HEvent found on method, but method is not public on " + this.listener.getClass().getName() + "." + this.method.getName() + "("+ CorePlugin.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ")");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        } catch (Throwable e){
            System.err.println("Failed to know what to do: HEvent found on method, but exception found when running " + this.listener.getClass().getName() + "." + this.method.getName() + "("+ CorePlugin.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ") found in plugin: " + this.plugin.getPluginName());
            e.printStackTrace();
        }
    }
}
