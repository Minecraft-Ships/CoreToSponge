package org.core.implementation.sponge.platform.plugin.boot.inject;

import org.core.implementation.sponge.platform.plugin.boot.inject.vanilla.VanillaPluginInjector;

public final class SpongeInjectors {

    public static final VanillaPluginInjector VANILLA = new VanillaPluginInjector();

    private SpongeInjectors() {
        throw new RuntimeException("Do not create");
    }
}
