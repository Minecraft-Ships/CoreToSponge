package org.core.implementation.sponge.platform.plugin.boot.inject;

import org.core.platform.plugin.CorePlugin;

public interface SpongeInjector {

    void inject(CorePlugin plugin);

    static void injectPlugin(CorePlugin plugin) {

    }

}
