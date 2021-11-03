package org.core.implementation.sponge.events;

import org.array.utils.ArrayUtils;
import org.core.event.Event;
import org.core.event.EventListener;
import org.core.platform.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SEventLaunch {

    protected final Plugin plugin;
    protected final EventListener listener;
    protected final Method method;

    public SEventLaunch(Plugin plugin, EventListener listener, Method method) {
        this.plugin = plugin;
        this.listener = listener;
        this.method = method;
    }

    public void run(Event event) {
        try {
            this.method.invoke(this.listener, event);
        } catch (IllegalAccessException e) {
            System.err.println("Failed to know what to do: HEvent found on method, but method is not public on " + this.listener.getClass().getName() + "." + this.method.getName() + "(" + ArrayUtils.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ")");
            e.printStackTrace();
        } catch (ExceptionInInitializerError e) {
            System.err.println("Failed to know what to do: EventListener caused exception from " + this.listener.getClass().getName() + "." + this.method.getName() + "(" + ArrayUtils.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ")");
            e.getException().printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("Failed to know what to do: EventListener caused exception from " + this.listener.getClass().getName() + "." + this.method.getName() + "(" + ArrayUtils.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ")");
            e.getTargetException().printStackTrace();
        } catch (Throwable e) {
            System.err.println("Failed to know what to do: HEvent found on method, but exception found when running " + this.listener.getClass().getName() + "." + this.method.getName() + "(" + ArrayUtils.toString(", ", p -> p.getType().getSimpleName() + " " + p.getName(), this.method.getParameters()) + ") found in plugin: " + this.plugin.getPluginName());
            e.printStackTrace();
        }
    }
}
