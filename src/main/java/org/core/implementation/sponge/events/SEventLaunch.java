package org.core.implementation.sponge.events;

import org.core.event.Event;
import org.core.event.EventListener;
import org.core.implementation.sponge.platform.plugin.boot.TranslateCoreBoot;
import org.core.platform.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

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
            TranslateCoreBoot.getBoot().logger().error("Failed to know what to do: HEvent found on method, but method is not public on " +
                    this.listener.getClass().getName() + "." + this.method.getName() + "(" + Arrays
                    .stream(this.method.getParameters())
                    .map(p -> p.getType().getSimpleName())
                    .collect(Collectors.joining(", ")) + ")");
            e.printStackTrace();
        } catch (ExceptionInInitializerError | InvocationTargetException e) {
            String parameterNames = Arrays
                    .stream(this.method.getParameters())
                    .map(p -> p.getType().getSimpleName() + " " + p.getName())
                    .collect(Collectors.joining(", "));
            TranslateCoreBoot.getBoot().logger().error("Failed to know what to do: EventListener caused exception from " +
                    this.listener.getClass().getName() + "." + this.method.getName() + "(" +
                    parameterNames + ")");
            if (e instanceof ExceptionInInitializerError) {
                ((ExceptionInInitializerError)e).getException().printStackTrace();
                return;
            }
            ((InvocationTargetException) e).getTargetException().printStackTrace();
        } catch (Throwable e) {
            String parameterNames = Arrays
                    .stream(this.method.getParameters())
                    .map(p -> p.getType().getSimpleName() + " " + p.getName())
                    .collect(Collectors.joining(", "));

            TranslateCoreBoot.getBoot().logger().error("Failed to know what to do: HEvent found on method, but exception found when running " +
                    this.listener.getClass().getName() + "." + this.method.getName() + "(" +
                    parameterNames + ") found in plugin: " + this.plugin.getPluginName());
            e.printStackTrace();
        }
    }
}
