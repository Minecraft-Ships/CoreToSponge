package org.core.implementation.sponge.platform.plugin.boot.inject;

import org.core.platform.plugin.CorePlugin;

import java.lang.reflect.InvocationTargetException;

public interface SpongeInjector {

    PluginContainerWrapper inject(CorePlugin plugin) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    static PluginContainerWrapper injectPlugin(CorePlugin plugin) {
        try {
            return SpongeInjectors.VANILLA.inject(plugin);
        } catch (NoSuchMethodException e) {
            //load forge
           /* try {
                ForgePluginInjector.injectPluginToPlatform(container);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }*/
            throw new RuntimeException(e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
