package org.core.implementation.sponge.platform.plugin.boot.inject.vanilla;

import org.core.implementation.sponge.platform.plugin.boot.inject.SpongeInjector;
import org.core.platform.plugin.CorePlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.plugin.PluginContainer;

import java.lang.reflect.InvocationTargetException;

public class VanillaPluginInjector implements SpongeInjector {
    @Override
    public VanillaPluginContainer inject(CorePlugin plugin) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        VanillaPluginContainer pluginContainer = new VanillaPluginContainer(plugin);

        PluginManager manager = Sponge.pluginManager();
        manager.getClass().getMethod("addPlugin", PluginContainer.class).invoke(manager, pluginContainer);

        return pluginContainer;
    }
}
